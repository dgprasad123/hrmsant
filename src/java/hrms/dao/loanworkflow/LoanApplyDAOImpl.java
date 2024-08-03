/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.loanworkflow;

//import com.sun.xml.ws.mex.MetadataConstants;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import hrms.SelectOption;
import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.controller.payroll.billbrowser.BillBrowserController;
import hrms.model.loanworkflow.LoanASanctionBean;
import hrms.model.loanworkflow.LoanForm;
import hrms.model.loanworkflow.LoanGPFForm;
import hrms.model.loanworkflow.LoanHBAForm;
import hrms.model.loanworkflow.LoanList;
import hrms.model.loanworkflow.LoanTempGPFForm;
import hrms.model.login.Users;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author lenovo
 */
public class LoanApplyDAOImpl implements LoanApplyDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;
    private Object DateTimeFormatter;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
   
    @Override
    public LoanForm displayEmpDetails(String hrmsid) {
        LoanForm loanvalue = new LoanForm();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Calendar now = Calendar.getInstance();
        int cmonth = now.get(Calendar.MONTH);
        int cyear = now.get(Calendar.YEAR);
        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("SELECT a.is_regular,a.emp_id,ARRAY_TO_STRING(ARRAY[INITIALS, a.F_NAME, a.M_NAME,a.L_NAME], ' ') fullname,a.cur_basic_salary,to_char(a.dob, 'DD-Mon-YYYY') as dob,to_char(a.dos, 'DD-Mon-YYYY') as dos,b.spn,b.spc,c.off_name  FROM emp_mast a,g_spc b,g_office c WHERE a.cur_spc=b.spc AND a.cur_off_code=c.off_code AND   emp_id=?");
            ps.setString(1, hrmsid);
            rs = ps.executeQuery();
            if (rs.next()) {
                loanvalue.setEmpName(rs.getString("fullname"));
                loanvalue.setBasicsalary(rs.getString("cur_basic_salary"));
                loanvalue.setDesignation(rs.getString("spn"));
                loanvalue.setEmpSPC(rs.getString("spc"));
                // des = StringUtils.strip(rs.getString("spn"), ",");
                //  loanvalue.setDesignation(rs.getString("des"));
                
                loanvalue.setOffaddress(rs.getString("off_name"));
                loanvalue.setNetsalary(getNetPay(hrmsid, cmonth, cyear) + "");
                if (rs.getString("is_regular").equals("Y")) {
                    loanvalue.setJobType("Permanent");
                } else {
                    loanvalue.setJobType("Temporary");
                }
                loanvalue.setEmpdob(rs.getString("dob"));
                loanvalue.setSuperannuation(rs.getString("dos"));

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return loanvalue;
    }

    public double getNetPay(String hrmsid, int monthValue, int yearValue) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        int basic = 0;
        int allowance = 0;
        int deduction = 0;
        int gross = 0;
        int net = 0;
        try {
            con = this.dataSource.getConnection();
            
            String sql = "SELECT CUR_BASIC FROM AQ_MAST WHERE EMP_CODE=? AND AQ_YEAR=? AND AQ_MONTH=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, hrmsid);
            pst.setInt(2, yearValue);
            pst.setInt(3, monthValue - 1);
            rs = pst.executeQuery();
            if (rs.next()) {
                basic = rs.getInt("CUR_BASIC");
            }

            sql = "SELECT AD_AMT FROM AQ_DTLS WHERE EMP_CODE=? AND AQ_YEAR=? AND AQ_MONTH=? AND AD_TYPE='A' AND AD_AMT > 0";
            pst = con.prepareStatement(sql);
            pst.setString(1, hrmsid);
            pst.setInt(2, yearValue);
            pst.setInt(3, monthValue - 1);
            rs = pst.executeQuery();
            while (rs.next()) {
                allowance = allowance + rs.getInt("AD_AMT");
            }
            gross = basic + allowance;

            sql = "SELECT AD_AMT FROM AQ_DTLS WHERE EMP_CODE=? AND AQ_YEAR=? AND AQ_MONTH=? AND AD_TYPE='D' AND AD_AMT > 0";
            pst = con.prepareStatement(sql);
            pst.setString(1, hrmsid);
            pst.setInt(2, yearValue);
            pst.setInt(3, monthValue - 1);
            rs = pst.executeQuery();
            while (rs.next()) {
                deduction = deduction + rs.getInt("AD_AMT");
            }
            net = gross - deduction;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return net;
    }

    @Override
    public void saveLoanData(LoanForm lform, String empid, String filepath) {
        Connection con = null;
        Calendar now = Calendar.getInstance();
        Date loanapplyDate = now.getTime();

        PreparedStatement ps = null;
        ResultSet rs = null;
        Date dt1 = null;
        Date dt2 = null;
        Date dt3 = null;

        DateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");

        MultipartFile loanAttch = lform.getFile_att();
        try {
            con = this.dataSource.getConnection();
            String forwardId = lform.getForwardtoHrmsid();
            String hidSpc = lform.getHidSPC();
            
            double antprice = Double.parseDouble(lform.getAntprice());
            String session_empId = empid;
            String designation = lform.getDesignation();
            double basicsalary = Double.parseDouble(lform.getBasicsalary());
            double netsalary = Double.parseDouble(lform.getNetsalary());
            String hidOffCode = lform.getHidOffCode();
            String hidOffName = lform.getHidOffName();

            String purtype = lform.getPurtype();
            String amountadv = lform.getAmountadv();

            int instalments = lform.getInstalments();
            String previousAvail = lform.getPreviousAvail();
            String PreAdvPur = lform.getPreAdvPur();
            String amounpretadv = lform.getAmounpretadv();

            String dateofdrawal = lform.getDateofdrawal();
            String intpaidfull = lform.getIntpaidfull();
            String amountstanding = lform.getAmountstanding();

            String officerleave = lform.getOfficerleave();
            String datecommleave = lform.getDatecommleave();
            String dateexpireleave = lform.getDateexpireleave();
            String forwardto = lform.getForwardto();
            int loanvalue = 25;
            String initiatedSpc = lform.getEmpSPC();
            String loanapplyfor = lform.getLoanapplyfor();

            /**
             * **********************************************************************************
             */
            String strTaskmaster = "INSERT INTO task_master (process_id "
                    + ",initiated_by "
                    + ",status_id "
                    + ",pending_at "
                    + ",apply_to "
                    + ",initiated_spc "
                    + ",pending_spc "
                    + ",apply_to_spc "
                    + ",initiated_on "
                    + ") values(?,?,?,?,?,?,?,?,?)";
            ps = con.prepareStatement(strTaskmaster, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, 6);
            ps.setString(2, session_empId);
            ps.setInt(3, loanvalue);
            ps.setString(4, forwardId);
            ps.setString(5, forwardId);
            ps.setString(6, initiatedSpc);
            ps.setString(7, hidSpc);
            ps.setString(8, hidSpc);
            ps.setTimestamp(9, new Timestamp(loanapplyDate.getTime()));
            int stsTask = ps.executeUpdate();
            rs = ps.getGeneratedKeys();
            rs.next();
            int taskId = rs.getInt("TASK_ID");

            /**
             * *************************************************************************************
             */
            //  dt2 = new Date(datecommleave);
            // dt3 = new Date(dateexpireleave);
            
            if (stsTask == 1) {

                String str = "INSERT INTO hw_emp_loan(emp_id "
                        + ",basic_salary "
                        + ",net_salary"
                        + ",ant_price"
                        + ",purchase_type"
                        + ",amount_adv"
                        + ",no_installment"
                        + ",loan_availed"
                        + ",date_drawal_adv"
                        + ",availed_amt_adv"
                        + ",principal_paid"
                        + ",loan_type"
                        + ",motorcar_cycle_moped"
                        + ",amount_standing"
                        + ",officer_leave"
                        + ",date_commencement"
                        + ",date_expire"
                        + ",forward_to"
                        + ",loan_status,loan_apply_date,taskid) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

                ps = con.prepareStatement(str);
                ps.setString(1, session_empId);
                ps.setDouble(2, basicsalary);
                ps.setDouble(3, netsalary);
                ps.setDouble(4, antprice);
                ps.setString(5, purtype);

                if (amountadv != null && !amountadv.equals("")) {
                    ps.setDouble(6, Double.parseDouble(amountadv));
                } else {
                    ps.setDouble(6, 0);
                }
                ps.setInt(7, instalments);
                ps.setString(8, previousAvail);
                if (lform.getDateofdrawal() != null && !lform.getDateofdrawal().equals("")) {
                    ps.setTimestamp(9, new Timestamp(sdf.parse(lform.getDateofdrawal()).getTime()));
                } else {
                    ps.setTimestamp(9, null);
                }
                if (amounpretadv != null && !amounpretadv.equals("")) {
                    ps.setDouble(10, Double.parseDouble(amounpretadv));
                } else {
                    ps.setDouble(10, 0);
                }
                ps.setString(11, intpaidfull);
                ps.setString(12, loanapplyfor);
                ps.setString(13, PreAdvPur);

                if (amountstanding != null && !amountstanding.equals("")) {
                    ps.setDouble(14, Double.parseDouble(amountstanding));
                } else {
                    ps.setDouble(14, 0);
                }
                ps.setString(15, officerleave);
                if (lform.getDatecommleave() != null && !lform.getDatecommleave().equals("")) {
                    ps.setTimestamp(16, new Timestamp(sdf.parse(lform.getDatecommleave()).getTime()));
                } else {
                    ps.setTimestamp(16, null);
                }
                if (lform.getDateexpireleave() != null && !lform.getDateexpireleave().equals("")) {
                    ps.setTimestamp(17, new Timestamp(sdf.parse(lform.getDateexpireleave()).getTime()));
                } else {
                    ps.setTimestamp(17, null);
                }
                ps.setString(18, hidSpc);
                ps.setInt(19, loanvalue);
                ps.setTimestamp(20, new Timestamp(loanapplyDate.getTime()));
                ps.setInt(21, taskId);
                int sts = ps.executeUpdate();

                if (sts > 0) {
                    InputStream inputStream = null;
                    OutputStream outputStream = null;
                    String filename = "";
                    if (loanAttch != null && !loanAttch.isEmpty()) {
                        long time = System.currentTimeMillis();
                        filename = session_empId + "_" + taskId + "_" + time;

                        ps = con.prepareStatement("UPDATE hw_emp_loan SET original_filename=?,disk_file_name=?,file_type=? WHERE TASKID=?");
                        ps.setString(1, loanAttch.getOriginalFilename());
                        ps.setString(2, filename);
                        ps.setString(3, loanAttch.getContentType());
                        ps.setInt(4, taskId);
                        ps.executeUpdate();

                        String dirpath = filepath + "/";
                        File newfile = new File(dirpath);
                        if (!newfile.exists()) {
                            newfile.mkdirs();
                        }

                        outputStream = new FileOutputStream(dirpath + filename);
                        int read = 0;
                        byte[] bytes = new byte[1024];
                        inputStream = loanAttch.getInputStream();
                        while ((read = inputStream.read(bytes)) != -1) {
                            outputStream.write(bytes, 0, read);
                        }
                    }
                }
            }

            /* ps = con.prepareStatement("SELECT a.emp_id,a.f_name  FROM emp_mast a,g_spc b WHERE a.cur_spc=b.spc AND b.spc=?");
             ps.setString(1, hidSpc);
             rs = ps.executeQuery();
             String applyToHrmsid=rs.getString("emp_id");*/
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public List getLoanList(String empid) {

        Connection con = null;

        Statement stmt = null;
        ResultSet rs = null;

        LoanList loan = null;
        ArrayList alist = new ArrayList();
        try {
            con = this.dataSource.getConnection();

            String sql = "SELECT a.*,to_char(a.loan_apply_date, 'DD-Mon-YYYY') as loan_apply_date_format FROM hw_emp_loan a WHERE emp_id='" + empid + "'";
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                loan = new LoanList();
                loan.setLoanid(rs.getString("loan_code"));
                loan.setLoanType(rs.getString("loan_type"));
                loan.setLoanDate(rs.getString("loan_apply_date_format"));
                loan.setPurType(rs.getString("purchase_type"));
                String loanStatus = loanStatus(rs.getInt("loan_status"), 6);
                loan.setLoanStatus(loanStatus);
                loan.setLoanstatusid(rs.getInt("loan_status"));
                loan.setTaskid(rs.getInt("taskid"));

                alist.add(loan);
            }

            String sqlgpf = "SELECT a.taskid,a.loan_status,a.loan_gpf_id as loan_code,a.gpftype as loan_type ,to_char(a.loan_apply_date, 'DD-Mon-YYYY') as loan_apply_date_format FROM hw_emp_gpf_loan a WHERE emp_id='" + empid + "'";
            stmt = con.createStatement();
            rs = stmt.executeQuery(sqlgpf);
            while (rs.next()) {
                loan = new LoanList();
                loan.setLoanid(rs.getString("loan_code"));
                String loanType = "GPF " + rs.getString("loan_type");
                loan.setLoanType(loanType);
                loan.setLoanDate(rs.getString("loan_apply_date_format"));

                String loanStatus = loanStatus(rs.getInt("loan_status"), 8);
                loan.setLoanStatus(loanStatus);
                loan.setLoanstatusid(rs.getInt("loan_status"));
                loan.setTaskid(rs.getInt("taskid"));

                alist.add(loan);
            }

            String sqltempgpf = "SELECT a.taskid,a.loan_status,a.loan_temp_gpf_id as loan_code,a.gpftype as loan_type ,to_char(a.loan_apply_date, 'DD-Mon-YYYY') as loan_apply_date_format FROM hw_emp_temp_gpf_loan a WHERE emp_id='" + empid + "'";
            stmt = con.createStatement();
            rs = stmt.executeQuery(sqltempgpf);
            while (rs.next()) {
                loan = new LoanList();
                loan.setLoanid(rs.getString("loan_code"));
                String loanType = rs.getString("loan_type");
                loan.setLoanType(loanType);
                loan.setLoanDate(rs.getString("loan_apply_date_format"));

                String loanStatus = loanStatus(rs.getInt("loan_status"), 10);
                loan.setLoanStatus(loanStatus);
                loan.setLoanstatusid(rs.getInt("loan_status"));
                loan.setTaskid(rs.getInt("taskid"));

                alist.add(loan);
            }

            String sqlhba = "SELECT a.taskid,a.loan_status,a.loan_hba_id as loan_code ,to_char(a.loan_apply_date, 'DD-Mon-YYYY') as loan_apply_date_format FROM hw_emp_hba_loan a WHERE emp_id='" + empid + "'";
            stmt = con.createStatement();
            rs = stmt.executeQuery(sqlhba);
            while (rs.next()) {
                loan = new LoanList();
                loan.setLoanid(rs.getString("loan_code"));
                String loanType = "Housing Building Advance ";
                loan.setLoanType(loanType);
                loan.setLoanDate(rs.getString("loan_apply_date_format"));

                String loanStatus = loanStatus(rs.getInt("loan_status"), 9);
                loan.setLoanStatus(loanStatus);
                loan.setLoanstatusid(rs.getInt("loan_status"));
                loan.setTaskid(rs.getInt("taskid"));

                alist.add(loan);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return alist;
    }

    @Override
    public String loanStatus(int statusid, int processid) {

        Connection con = null;

        Statement stmt = null;
        ResultSet rs = null;
        String loanstatus = null;
        try {
            con = this.dataSource.getConnection();

            String sql = "SELECT status_name FROM g_process_status  WHERE status_id='" + statusid + "' AND process_id='" + processid + "'";
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                loanstatus = rs.getString("status_name");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return loanstatus;

    }

    @Override
    public LoanForm getLoanDetails(int taskid, String hrmsid) {

        LoanForm loanvalue = new LoanForm();
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement pstask = null;
        PreparedStatement psapply = null;
        ResultSet rs = null;
        ResultSet rstask = null;
        ResultSet rsapply = null;
        Calendar now = Calendar.getInstance();
        int cmonth = now.get(Calendar.MONTH);
        int cyear = now.get(Calendar.YEAR);
        String apply_hrmsid = null;
        int statusId = 0;
        int loanid = 0;

        try {
            con = this.dataSource.getConnection();
            pstask = con.prepareStatement("SELECT a.*,to_char(a.date_drawal_adv, 'DD-Mon-YYYY') as date_drawal_format,to_char(a.date_commencement, 'DD-Mon-YYYY') as date_commencement_format,to_char(a.date_expire, 'DD-Mon-YYYY') as date_expire_format,b.* FROM hw_emp_loan a,task_master b WHERE a.taskid=b.task_id AND a.taskid='" + taskid + "' ");
            rstask = pstask.executeQuery();
            if (rstask.next()) {
                apply_hrmsid = rstask.getString("emp_id");
                loanvalue.setEmpID(apply_hrmsid);
                loanvalue.setAntprice(rstask.getString("ant_price"));
                loanvalue.setPurtype(rstask.getString("purchase_type"));
                loanvalue.setAmountadv(rstask.getString("amount_adv"));
                loanvalue.setInstalments(rstask.getInt("no_installment"));
                loanvalue.setPreviousAvail(rstask.getString("loan_availed"));
                loanvalue.setPreAdvPur(rstask.getString("motorcar_cycle_moped"));
                loanvalue.setAmounpretadv(rstask.getString("availed_amt_adv"));
                loanvalue.setDateofdrawal(rstask.getString("date_drawal_format"));
                loanvalue.setIntpaidfull(rstask.getString("principal_paid"));
                loanvalue.setAmountstanding(rstask.getString("amount_standing"));
                loanvalue.setOfficerleave(rstask.getString("officer_leave"));
                loanvalue.setDatecommleave(rstask.getString("date_commencement_format"));
                loanvalue.setDateexpireleave(rstask.getString("date_expire_format"));
                loanvalue.setLoanId(rstask.getInt("loan_code"));
                loanvalue.setTaskid(rstask.getInt("taskid"));
                loanvalue.setStatusId(rstask.getInt("status_id"));
                loanvalue.setApprovedBy(rstask.getString("apply_to"));
                loanvalue.setApprovedSpc(rstask.getString("pending_spc"));
                loanvalue.setNotes(rstask.getString("note"));
                if (rstask.getString("disk_file_name") != null && !rstask.getString("disk_file_name").equals("")) {
                    loanvalue.setDiskFileName(rstask.getString("disk_file_name"));
                } else {
                    loanvalue.setDiskFileName("");
                }
                loanvalue.setLoancomments(rstask.getString("comments"));
                
                loanvalue.setLoanapplyfor(rstask.getString("loan_type"));
                statusId = rstask.getInt("status_id");
                loanid = rstask.getInt("loan_code");

            }
            /**
             * ********************************** Apply user Details
             * *********************************
             */
            psapply = con.prepareStatement("SELECT ARRAY_TO_STRING(ARRAY[INITIALS, a.F_NAME, a.M_NAME,a.L_NAME], ' ') fullname,a.cur_basic_salary,to_char(a.dob, 'DD-Mon-YYYY') as dob,to_char(a.dos, 'DD-Mon-YYYY') as dos,b.spn,b.spc,c.off_name  FROM emp_mast a,g_spc b,g_office c WHERE a.cur_spc=b.spc AND a.cur_off_code=c.off_code AND   emp_id=?");
            psapply.setString(1, rstask.getString("apply_to"));
            rsapply = psapply.executeQuery();
            if (rsapply.next()) {
                String apply_name = rsapply.getString("fullname");
                String apply_spn = rsapply.getString("spn");
                String apply_emp = apply_name + " (" + apply_spn + ")";
                loanvalue.setForwardtoHrmsid(apply_emp);
            }

            ps = con.prepareStatement("SELECT a.is_regular,a.emp_id,ARRAY_TO_STRING(ARRAY[INITIALS, a.F_NAME, a.M_NAME,a.L_NAME], ' ') fullname,a.cur_basic_salary,to_char(a.dob, 'DD-Mon-YYYY') as dob,to_char(a.dos, 'DD-Mon-YYYY') as dos,b.spn,b.spc,c.off_name  FROM emp_mast a,g_spc b,g_office c WHERE a.cur_spc=b.spc AND a.cur_off_code=c.off_code AND   emp_id=?");
            ps.setString(1, rstask.getString("emp_id"));
            rs = ps.executeQuery();

            if (rs.next()) {
                loanvalue.setEmpName(rs.getString("fullname"));
                loanvalue.setBasicsalary(rs.getString("cur_basic_salary"));
                loanvalue.setDesignation(rs.getString("spn"));
                loanvalue.setEmpSPC(rs.getString("spc"));
                // des = StringUtils.strip(rs.getString("spn"), ",");
                //  loanvalue.setDesignation(rs.getString("des"));
                
                loanvalue.setOffaddress(rs.getString("off_name"));
                loanvalue.setNetsalary(getNetPay(apply_hrmsid, cmonth, cyear) + "");
                if (rs.getString("is_regular").equals("Y")) {
                    loanvalue.setJobType("Permanent");
                } else {
                    loanvalue.setJobType("Temporary");
                }
                loanvalue.setEmpdob(rs.getString("dob"));
                loanvalue.setSuperannuation(rs.getString("dos"));

            }
            
            if (statusId == 92 || statusId == 97) {

                pstask = con.prepareStatement("SELECT a.*,to_char(a.sanction_date, 'DD-Mon-YYYY') as fsanction_date,to_char(a.recovery_sdate, 'DD-Mon-YYYY') as frecovery_sdate FROM hw_loan_sanction_details a WHERE  a.taskid=? AND a.apply_loanid=? ");
                pstask.setInt(1, taskid);
                pstask.setInt(2, loanid);
                rstask = pstask.executeQuery();
                if (rstask.next()) {
                    loanvalue.setLetterNo(rstask.getString("sanction_no"));
                    loanvalue.setLetterDate(rstask.getString("fsanction_date"));
                    loanvalue.setSamount(rstask.getString("sanction_amount"));
                    loanvalue.setReleaseAmount(rstask.getString("release_amount"));
                    loanvalue.setEmiPrincipal(rstask.getInt("prinicipal_installment"));
                    loanvalue.setPrincipalAmount(rstask.getString("prinicipal_installment_amt"));
                    loanvalue.setTotalInterestamount(rstask.getString("interestamount"));
                    loanvalue.setEmiInterest(rstask.getInt("interest_installment"));
                    loanvalue.setInterestAmount(rstask.getString("interest_installment_amt"));
                    loanvalue.setInterestType(rstask.getString("interesttype"));
                    loanvalue.setRateInterest(rstask.getString("rate_interest"));
                    loanvalue.setLastInstallment(rstask.getString("last_installment_amt"));
                    loanvalue.setPenalRate(rstask.getString("penal_rate_interest"));
                    loanvalue.setMoratoriumRequired(rstask.getString("moratorium_required"));
                    loanvalue.setMoratoriumPeriod(rstask.getString("moratorium_period"));
                    loanvalue.setInsuranceFlag(rstask.getString("insurance_flag"));
                    loanvalue.setRecDate(rstask.getString("frecovery_sdate"));
                    loanvalue.setChatofAccount(rstask.getString("chat_account"));
                    loanvalue.setBillid(rstask.getString("bill_id"));
                    loanvalue.setDemandNo(rstask.getString("demand_no"));
                    loanvalue.setMajorhead(rstask.getString("major_head"));

                    // loanvalue.se
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return loanvalue;

    }

    @Override
    public List getPostList(String offcode) {

        Connection con = null;

        PreparedStatement ps = null;
        ResultSet rs = null;

        List li = new ArrayList();
        SelectOption so = null;
        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("select spc,post,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMPNAME,emp_id from ("
                    + "select spc,gpc from g_spc where off_code='" + offcode + "' AND (IFUCLEAN!='Y' OR IFUCLEAN IS NULL)) g_spc "
                    + "inner join g_post on g_spc.GPC = g_post.POST_CODE "
                    + "left outer join EMP_MAST ON G_SPC.SPC=EMP_MAST.CUR_SPC ORDER BY POST");
            rs = ps.executeQuery();
            while (rs.next()) {
                if (rs.getString("EMPNAME") != null && !rs.getString("EMPNAME").equals("")) {
                    so = new SelectOption();
                    so.setLabel(rs.getString("post"));
                    so.setDesc(rs.getString("EMPNAME"));
                    String SpcHrmsId = rs.getString("spc") + "|" + rs.getString("emp_id");
                    so.setValue(SpcHrmsId);
                    li.add(so);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return li;
    }

    @Override
    public List getprocessList(String processid, String taskidlist) {

        Connection con = null;

        PreparedStatement ps = null;
        ResultSet rs = null;

        List li = new ArrayList();
        SelectOption so = null;
        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement(" SELECT * FROM g_process_status WHERE status_name!='PENDING' AND process_id='" + processid + "' AND status_id IN (" + taskidlist + ")");
            rs = ps.executeQuery();
            while (rs.next()) {
                if (rs.getString("action_name") != null && !rs.getString("action_name").equals("")) {
                    so = new SelectOption();
                    so.setLabel(rs.getString("action_name"));

                    so.setValue(rs.getString("status_id"));
                    li.add(so);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return li;
    }

    @Override
    public List getloanSanctionOperator() {

        Connection con = null;

        PreparedStatement ps = null;
        ResultSet rs = null;

        List li = new ArrayList();
        SelectOption so = null;
        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement(" SELECT * FROM g_loan_sanction_operator ORDER BY authority_desc");
            rs = ps.executeQuery();
            while (rs.next()) {
                if (rs.getString("authority_desc") != null && !rs.getString("authority_desc").equals("")) {
                    so = new SelectOption();
                    String authName = rs.getString("authority_desc") + "(" + rs.getString("ddo_code") + ")";
                    String loginName = rs.getString("login_id") + "|" + rs.getString("ddo_code");
                    so.setLabel(authName);
                    so.setValue(loginName);
                    li.add(so);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return li;
    }

    public void saveApproveLoanData(LoanForm lform, String empid) {
        Connection con = null;
        Calendar now = Calendar.getInstance();
        Date loanapplyDate = now.getTime();

        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        PreparedStatement ps2 = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        ResultSet rs2 = null;
        Date dt1 = null;
        String pending_at = null;
        String pending_spc = null;
        int taskid = lform.getTaskid();
        int loanid = lform.getLoanId();
        int loanstatus = lform.getLoan_status();
        String Comments = lform.getLoancomments();
        String approvedby = lform.getApprovedBy();
        String approvedspc = lform.getApprovedSpc();
        String gpfSeries = "";
        String gpfnumber = "";
        DateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");

        MultipartFile loanAttch = lform.getFile_att();
        try {
            con = this.dataSource.getConnection();

            if (loanstatus == 29 || loanstatus == 27 || loanstatus == 92 || loanstatus == 97) {
                String forwardId = lform.getForwardtoHrmsid();
                String hidSpc = lform.getHidSPC();
                ps = con.prepareStatement(" SELECT * FROM task_master WHERE  task_id='" + taskid + "'");
                rs = ps.executeQuery();
                while (rs.next()) {
                    pending_at = (rs.getString("pending_at"));
                    pending_spc = (rs.getString("pending_spc"));
                }
                ps = con.prepareStatement("UPDATE task_master SET status_id=?,pending_at=?,note=?,apply_to=?,pending_spc=?,apply_to_spc=? WHERE task_id=?");
                ps.setInt(1, loanstatus);
                ps.setString(2, forwardId);
                ps.setString(3, Comments);
                ps.setString(4, forwardId);
                ps.setString(5, hidSpc);
                ps.setString(6, hidSpc);
                ps.setInt(7, taskid);
                int stsTask = ps.executeUpdate();
                if (stsTask == 1) {
                    String str = "INSERT INTO workflow_log(task_id "
                            + ",task_action_date "
                            + ",action_taken_by"
                            + ",spc_ontime"
                            + ",task_status_id"
                            + ",note"
                            + ",forward_to"
                            + ",forwarded_spc"
                           
                            + ",ref_id"
                            + ") values(?,?,?,?,?,?,?,?,?)";
                    ps = con.prepareStatement(str);
                    ps.setInt(1, taskid);
                    ps.setTimestamp(2, new Timestamp(loanapplyDate.getTime()));
                    ps.setString(3, pending_at);
                    ps.setString(4, pending_spc);
                    ps.setInt(5, loanstatus);
                    ps.setString(6, Comments);
                    ps.setString(7, forwardId);
                    ps.setString(8, hidSpc);
                    ps.setInt(10, loanid);
                  
                    ps.executeUpdate();

                    ps = con.prepareStatement("UPDATE hw_emp_loan SET loan_status=? WHERE taskid=? AND loan_code=?");
                    ps.setInt(1, loanstatus);
                    ps.setInt(2, taskid);
                    ps.setInt(3, loanid);
                    ps.executeUpdate();
                    if (loanstatus == 27) {
                        ps = con.prepareStatement("SELECT * FROM hw_emp_loan WHERE  taskid=? AND loan_code=? LIMIT 1");
                        ps.setInt(1, taskid);
                        ps.setInt(2, loanid);
                        rs = ps.executeQuery();
                        if (rs.next()) {
                            Date approverdate = now.getTime();
                            String loanApplyId = rs.getString("emp_id");
                            String loanshortcode = "";
                            String chatofaccount = "";
                            String majorhead = "";
                            String billid = "";
                            if (rs.getString("loan_type").equals("PERSONAL COMPUTER")) {
                                loanshortcode = "CMPA";
                                chatofaccount = "057610002040825480570001110";
                                majorhead = "7610";
                                billid = "08";
                            }
                            if (rs.getString("loan_type").equals("MOTOR CYCLE")) {
                                loanshortcode = "MCA";
                                chatofaccount = "057610002020020480010001110";
                                majorhead = "7610";
                                billid = "07";
                            }
                            if (rs.getString("loan_type").equals("MOTOR MOPED")) {
                                loanshortcode = "MCA";
                                chatofaccount = "057610002020020480010001110";
                                majorhead = "7610";
                                billid = "07";
                            }
                            if (rs.getString("loan_type").equals("MOTOR CAR")) {
                                loanshortcode = "VE";
                                chatofaccount = "057610002020020480010001110";
                                majorhead = "7610";
                                billid = "06";
                            }

                            Users ue = getLoanEmpDetail(loanApplyId);
                            String ifmsLoanId = loanshortcode + ue.getGpfno();

                            String str1 = "INSERT INTO hw_loan_sanction_details(taskid "
                                    + ",apply_loanId "
                                    + ",loan_type"
                                    + ",loanee_id"
                                    + ",approver_id"
                                    + ",approved_date"
                                    + ",gpfNo"
                                    + ",accountNo"
                                    + ",pranNo"
                                    + ",name"
                                    + ",designation"
                                    + ",office_name"
                                    + ",dob"
                                    + ",dos"
                                    + ",basic_pay"
                                    + ",gp"
                                    + ",empType"
                                    + ",address"
                                    + ",gpf_series"
                                    + ",gpf_accountno"
                                    + ",ifms_loanid"
                                    + ",demand_no"
                                    + ",major_head"
                                    + ",chat_account"
                                    + ",bill_id"
                                    + ",ddo_code"
                                    + ",bank_account"
                                    + ",ifsc_code"
                                    + ",mobile"
                                    + ") values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                            ps1 = con.prepareStatement(str1);
                            ps1.setInt(1, taskid);
                            ps1.setInt(2, loanid);
                            ps1.setString(3, rs.getString("loan_type"));
                            ps1.setString(4, rs.getString("emp_id"));
                            ps1.setString(5, empid);
                            ps1.setTimestamp(6, new Timestamp(approverdate.getTime()));
                            if (ue.getAcctType().equals("GPF")) {
                                gpfnumber = getempNumberFromGPF(ue.getGpfno());
                                gpfSeries = CommonFunctions.getGPFSeries(ue.getGpfno());
                                ps1.setString(7, ue.getGpfno());
                                ps1.setString(8, ue.getAcctType());
                                ps1.setString(9, "");

                            } else {
                                ps1.setString(7, "");
                                ps1.setString(8, ue.getAcctType());
                                ps1.setString(9, ue.getGpfno());
                                gpfnumber = ue.getGpfno();

                            }

                            ps1.setString(10, ue.getFullName());
                            ps1.setString(11, ue.getPostname());
                            ps1.setString(12, ue.getOffname());
                            ps1.setTimestamp(13, new Timestamp(sdf.parse(ue.getEmpDob()).getTime()));
                            ps1.setTimestamp(14, new Timestamp(sdf.parse(ue.getEmpDos()).getTime()));
                            ps1.setDouble(15, Double.parseDouble(ue.getBasicsalry()));
                            ps1.setDouble(16, Double.parseDouble(ue.getGp()));
                            // ps1.setString(15, rs.getString("cur_basic_salary"));
                            // ps1.setString(16, rs.getString("gp"));
                            if (ue.getIsRegular().equals("Y")) {
                                ps1.setString(17, "Permanent");
                                //	loanvalue.setJobType("Permanent");
                            } else {
                                ps1.setString(17, "Temporary");
                                //loanvalue.setJobType("Temporary");
                            }
                            ps1.setString(18, ue.getAddress());
                            ps1.setString(19, gpfSeries);
                            ps1.setString(20, gpfnumber);
                            ps1.setString(21, ifmsLoanId);
                            ps1.setString(22, "05");
                            ps1.setString(23, majorhead);
                            ps1.setString(24, chatofaccount);
                            ps1.setString(25, billid);
                            ps1.setString(26, ue.getDdoCode());
                            ps1.setString(27, ue.getBankAccNo());
                            ps1.setString(28, ue.getIfmsCode());
                            ps1.setString(29, ue.getMobile());
                            ps1.executeUpdate();
                        }
                        //  System.exit(0); 

                    }
                    if (loanstatus == 92) {
                        Date entryDate = now.getTime();
                        ps1 = con.prepareStatement("UPDATE hw_loan_sanction_details SET issue_letter_login_id=?,issue_letter_entry_date=?,sanction_no=?,sanction_date=? WHERE taskid=? AND apply_loanid=?");

                        ps1.setString(1, empid);
                        ps1.setTimestamp(2, new Timestamp(entryDate.getTime()));

                        ps1.setString(3, lform.getLetterNo());
                        if (lform.getLetterDate() != null && !lform.getLetterDate().equals("")) {
                            ps1.setTimestamp(4, new Timestamp(sdf.parse(lform.getLetterDate()).getTime()));
                        } else {
                            ps1.setTimestamp(4, null);
                        }
                        ps1.setInt(5, taskid);
                        ps1.setInt(6, loanid);
                        ps1.executeUpdate();

                    }

                    if (loanstatus == 97) {
                        Date entryDate = now.getTime();
                        ps1 = con.prepareStatement("UPDATE hw_loan_sanction_details SET loan_sanction_login_id=?,sanction_entry_date=?,interesttype=?,sanction_amount=?,release_amount=?,prinicipal_installment=?,prinicipal_installment_amt=?,interest_installment=?,interest_installment_amt=?,rate_interest=?,last_installment_amt=?,penal_rate_interest=?,moratorium_required=?,moratorium_period=?,insurance_flag=?,recovery_sdate=?,interestamount=? WHERE taskid=? AND apply_loanid=?");

                        ps1.setString(1, empid);
                        ps1.setTimestamp(2, new Timestamp(entryDate.getTime()));
                        ps1.setString(3, lform.getInterestType());
                        String sanction_amount = lform.getSamount();
                        String release_amount = lform.getRamount();
                        if (sanction_amount != null && !sanction_amount.equals("")) {
                            ps1.setDouble(4, Double.parseDouble(sanction_amount));
                        } else {
                            ps1.setDouble(4, 0);
                        }
                        if (release_amount != null && !release_amount.equals("")) {
                            ps1.setDouble(5, Double.parseDouble(release_amount));
                        } else {
                            ps1.setDouble(5, 0);
                        }
                        ps1.setInt(6, lform.getEmiPrincipal());
                        String PrincipalAmount = lform.getPrincipalAmount();
                        ps1.setDouble(7, Double.parseDouble(PrincipalAmount));
                        ps1.setInt(8, lform.getEmiInterest());
                        ps1.setDouble(9, Double.parseDouble(lform.getInterestAmount()));
                        ps1.setDouble(10, Double.parseDouble(lform.getRateInterest()));
                        String last_installment_amt = lform.getLastInstallment();
                        if (last_installment_amt != null && !last_installment_amt.equals("")) {
                            ps1.setDouble(11, Double.parseDouble(last_installment_amt));
                        } else {
                            ps1.setDouble(11, 0);
                        }
                        String penal_rate_interest = lform.getPenalRate();
                        if (penal_rate_interest != null && !penal_rate_interest.equals("")) {
                            ps1.setDouble(12, Double.parseDouble(penal_rate_interest));
                        } else {
                            ps1.setDouble(12, 0);
                        }
                        ps1.setString(13, lform.getMoratoriumRequired());
                        ps1.setString(14, lform.getMoratoriumPeriod());
                        ps1.setString(15, lform.getInsuranceFlag());
                        // String recDate = lform.getRecDate();
                        if (lform.getRecDate() != null && !lform.getRecDate().equals("")) {
                            ps1.setTimestamp(16, new Timestamp(sdf.parse(lform.getRecDate()).getTime()));
                        } else {
                            ps1.setTimestamp(16, null);
                        }

                        ps1.setDouble(17, Double.parseDouble(lform.getTotalInterestamount()));

                        ps1.setInt(18, taskid);
                        ps1.setInt(19, loanid);
                        ps1.executeUpdate();

                    }

                }

            } else {

                ps = con.prepareStatement("UPDATE hw_emp_loan SET loan_status=?,approved_by=?,approved_spc=?,comments=?,loan_sanction_date=?,letterno=?,letterdate=? WHERE taskid=? AND loan_code=?");
                ps.setInt(1, loanstatus);
                ps.setString(2, approvedby);
                ps.setString(3, approvedspc);
                ps.setString(4, Comments);
                ps.setTimestamp(5, new Timestamp(loanapplyDate.getTime()));
                ps.setString(6, lform.getLetterNo());
                ps.setString(7, lform.getLetterDate());
                ps.setInt(8, taskid);
                ps.setInt(9, loanid);
                int stsTask = ps.executeUpdate();

                if (stsTask == 1) {
                    ps = con.prepareStatement("UPDATE task_master SET status_id=?,pending_at=?,note=?,apply_to=?,pending_spc=?,apply_to_spc=? WHERE task_id=?");
                    ps.setInt(1, loanstatus);
                    ps.setString(2, null);
                    ps.setString(3, Comments);
                    ps.setString(4, null);
                    ps.setString(5, null);
                    ps.setString(6, null);
                    ps.setInt(7, taskid);
                    ps.executeUpdate();

                    //  ps1 = con.prepareStatement("UPDATE hw_loan_sanction_details SET loan_entrey_id=?,entry_date=?,interesttype=?,sanction_amount=?,release_amount=?,prinicipal_installment=? WHERE task_id=?");
                }    

                if (loanstatus == 26) {
                    Date entryDate = now.getTime();
                    ps1 = con.prepareStatement("UPDATE hw_loan_sanction_details SET bill_create_id=?,bill_entry_date=?,bill_no=?,bill_date=?,bill_amount=?,bill_description=? WHERE taskid=? AND apply_loanid=?");

                    ps1.setString(1, empid);
                    ps1.setTimestamp(2, new Timestamp(entryDate.getTime()));
                    ps1.setString(3, lform.getBillno());
                    if (lform.getBilldate() != null && !lform.getBilldate().equals("")) {
                        ps1.setTimestamp(4, new Timestamp(sdf.parse(lform.getBilldate()).getTime()));
                    } else {
                        ps1.setTimestamp(4, null);
                    }

                    String bill_amount = lform.getBillAmount();
                    if (bill_amount != null && !bill_amount.equals("")) {
                        ps1.setDouble(5, Double.parseDouble(bill_amount));
                    } else {
                        ps1.setDouble(5, 0);
                    }

                    ps1.setString(6, lform.getBillDescription());
                    ps1.setInt(7, taskid);
                    ps1.setInt(8, loanid);
                    ps1.executeUpdate();

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public LoanForm ReplyLoan(String option, String hrmsid, int loanid) {

        LoanForm loanvalue = new LoanForm();
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement psapply = null;
        ResultSet rs = null;
        ResultSet rsapply = null;
        Calendar now = Calendar.getInstance();
        int cmonth = now.get(Calendar.MONTH);
        int cyear = now.get(Calendar.YEAR);
        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("SELECT a.is_regular,a.emp_id,ARRAY_TO_STRING(ARRAY[INITIALS, a.F_NAME, a.M_NAME,a.L_NAME], ' ') fullname,a.cur_basic_salary,to_char(a.dob, 'DD-Mon-YYYY') as dob,to_char(a.dos, 'DD-Mon-YYYY') as dos,b.spn,b.spc,c.off_name  FROM emp_mast a,g_spc b,g_office c WHERE a.cur_spc=b.spc AND a.cur_off_code=c.off_code AND   emp_id=?");
            ps.setString(1, hrmsid);
            rs = ps.executeQuery();
            if (rs.next()) {
                loanvalue.setEmpName(rs.getString("fullname"));
                loanvalue.setBasicsalary(rs.getString("cur_basic_salary"));
                loanvalue.setDesignation(rs.getString("spn"));
                loanvalue.setEmpSPC(rs.getString("spc"));
                // des = StringUtils.strip(rs.getString("spn"), ",");
                //  loanvalue.setDesignation(rs.getString("des"));
                
                loanvalue.setOffaddress(rs.getString("off_name"));
                loanvalue.setNetsalary(getNetPay(hrmsid, cmonth, cyear) + "");
                if (rs.getString("is_regular").equals("Y")) {
                    loanvalue.setJobType("Permanent");
                } else {
                    loanvalue.setJobType("Temporary");
                }
                loanvalue.setEmpdob(rs.getString("dob"));
                loanvalue.setSuperannuation(rs.getString("dos"));

            }
            ps = con.prepareStatement("SELECT a.*,b.*,to_char(a.date_drawal_adv, 'DD-Mon-YYYY') as date_drawal_format,to_char(a.date_commencement, 'DD-Mon-YYYY') as date_commencement_format,to_char(a.date_expire, 'DD-Mon-YYYY') as date_expire_format FROM hw_emp_loan a,task_master b WHERE a.taskid=b.task_id AND  a.loan_code=?");
            ps.setInt(1, loanid);
            rs = ps.executeQuery();
            if (rs.next()) {
                loanvalue.setLoanId(loanid);
                loanvalue.setInstalments(rs.getInt("no_installment"));
                loanvalue.setAntprice(rs.getString("ant_price"));

                // String Ptype=rs.getString("purchase_type");                  
                loanvalue.setPurtype(rs.getString("purchase_type"));

                loanvalue.setAmountadv(rs.getDouble("amount_adv") + "");
                loanvalue.setPreviousAvail(rs.getString("loan_availed"));
                loanvalue.setPreAdvPur(rs.getString("motorcar_cycle_moped"));
                loanvalue.setAmounpretadv(rs.getDouble("availed_amt_adv") + "");
                loanvalue.setDateofdrawal(rs.getString("date_drawal_format"));
                loanvalue.setIntpaidfull(rs.getString("principal_paid"));
                loanvalue.setAmountstanding(rs.getDouble("amount_standing") + "");
                loanvalue.setOfficerleave(rs.getString("officer_leave"));
                loanvalue.setDatecommleave(rs.getString("date_commencement_format"));
                loanvalue.setDateexpireleave(rs.getString("date_expire_format"));
                loanvalue.setFileView(rs.getString("original_filename"));
                loanvalue.setTaskid(rs.getInt("taskid"));
                loanvalue.setLoancomments(rs.getString("note"));
                loanvalue.setDiskFileName(rs.getString("disk_file_name"));
                loanvalue.setLoanapplyfor(rs.getString("loan_type"));

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return loanvalue;
    }

    @Override
    public LoanForm SactionLoanOrder(int taskid, String hrmsid, int loanid) {

        LoanForm loanvalue = new LoanForm();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        loanvalue.setTaskid(taskid);
        loanvalue.setLoanId(loanid);

        try {
            con = this.dataSource.getConnection();

            ps = con.prepareStatement("SELECT a.* FROM hw_emp_loan a WHERE loan_code=?");
            ps.setInt(1, loanid);

            rs = ps.executeQuery();
            if (rs.next()) {
                loanvalue.setInstalments(rs.getInt("no_installment"));
                hrmsid = rs.getString("emp_id");
                Double amount_adv = rs.getDouble("amount_adv");
                loanvalue.setAmounpretadv(Math.round(amount_adv) + "/-");
                Double amount_inst = amount_adv / rs.getInt("no_installment");

                String amt = "RS " + Math.round(amount_inst) + "/-";
                loanvalue.setAmountadv(amt);
                loanvalue.setPreAdvPur(rs.getString("motorcar_cycle_moped"));
                loanvalue.setInstalments(rs.getInt("no_installment"));
                loanvalue.setLetterNo(rs.getString("letterno"));
                loanvalue.setLetterDate(rs.getString("letterdate"));
                loanvalue.setLetterformName(rs.getString("letterfrom"));
                loanvalue.setLetterformdesignation(rs.getString("fromdesignation"));
                loanvalue.setLetterto(rs.getString("letterto"));
                loanvalue.setMemoNo(rs.getString("memono"));
                loanvalue.setMemoDate(rs.getString("memodate"));

            }

            // ps = con.prepareStatement("SELECT a.is_regular,gpf_no,a.gp,a.emp_id,ARRAY_TO_STRING(ARRAY[INITIALS, a.F_NAME, a.M_NAME,a.L_NAME], ' ') fullname,a.cur_basic_salary,to_char(a.dob, 'DD-Mon-YYYY') as dob,to_char(a.dos, 'DD-Mon-YYYY') as dos,b.spn,b.spc,b.gpc,c.post  FROM emp_mast a,g_spc b,g_post c WHERE  a.cur_spc=b.spc AND b.gpc=c.post_code   AND     emp_id=?");
            ps = con.prepareStatement("SELECT a.is_regular,a.gpf_no,a.gp,a.emp_id,ARRAY_TO_STRING(ARRAY[INITIALS, a.F_NAME, a.M_NAME,"
                    + "a.L_NAME], ' ') fullname,to_char(a.dob, 'DD-Mon-YYYY') as dob,a.cur_basic_salary,to_char(a.JOINDATE_OF_GOO, 'DD-Mon-YYYY') as doj,"
                    + "to_char(a.dos, 'DD-Mon-YYYY') as dos,b.spn,b.spc,b.gpc,c.post,d.department_name,"
                    + "EXTRACT(YEAR from AGE(NOW(), a.JOINDATE_OF_GOO)) as age,EXTRACT(YEAR from AGE(a.dos,NOW())) as remainingage,e.ddo_code,e.off_en  FROM emp_mast a,g_spc b,g_post c,"
                    + "g_department d,g_office e WHERE  a.cur_spc=b.spc AND b.dept_code=d.department_code AND  "
                    + " b.gpc=c.post_code AND a.cur_off_code=e.off_code AND emp_id=?");

            ps.setString(1, hrmsid);
            rs = ps.executeQuery();
            if (rs.next()) {
                String Loanee_name = rs.getString("fullname");

                String Loanee_des = rs.getString("post");
                String loanee_name_des = Loanee_name + "<br/>" + Loanee_des;
                loanvalue.setEmpName(loanee_name_des);

                String loanee_basicsalary = rs.getString("cur_basic_salary");
                String loanee_gp = rs.getString("gp");

                String basic_gp = "RS " + loanee_basicsalary + "/-" + "<br/>" + "+" + "<br/>" + "RS " + loanee_gp + "/-";
                loanvalue.setBasicsalary(basic_gp);
                if (rs.getString("is_regular").equals("Y")) {
                    loanvalue.setJobType("Perm.");
                } else {
                    loanvalue.setJobType("Temp.");
                }

                loanvalue.setGpfno(rs.getString("gpf_no"));

                loanvalue.setEmpdob(rs.getString("dob"));
                loanvalue.setOffaddress(rs.getString("department_name"));
                loanvalue.setName(Loanee_name);

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }

        return loanvalue;
    }

    @Override
    public LoanForm PreviewSactionOrder(int taskid, String hrmsid, int loanid) {

        LoanForm loanvalue = new LoanForm();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        loanvalue.setTaskid(taskid);
        loanvalue.setLoanId(loanid);
        try {
            con = this.dataSource.getConnection();
            // ps = con.prepareStatement("SELECT a.is_regular,gpf_no,a.gp,a.emp_id,ARRAY_TO_STRING(ARRAY[INITIALS, a.F_NAME, a.M_NAME,a.L_NAME], ' ') fullname,a.cur_basic_salary,to_char(a.dob, 'DD-Mon-YYYY') as dob,to_char(a.dos, 'DD-Mon-YYYY') as dos,b.spn,b.spc,b.gpc,c.post  FROM emp_mast a,g_spc b,g_post c WHERE  a.cur_spc=b.spc AND b.gpc=c.post_code   AND     emp_id=?");
            ps = con.prepareStatement("SELECT a.is_regular,a.gpf_no,a.gp,a.emp_id,ARRAY_TO_STRING(ARRAY[INITIALS, a.F_NAME, a.M_NAME,"
                    + "a.L_NAME], ' ') fullname,to_char(a.dob, 'DD-Mon-YYYY') as dob,a.cur_basic_salary,to_char(a.JOINDATE_OF_GOO, 'DD-Mon-YYYY') as doj,"
                    + "to_char(a.dos, 'DD-Mon-YYYY') as dos,b.spn,b.spc,b.gpc,c.post,d.department_name,"
                    + "EXTRACT(YEAR from AGE(NOW(), a.JOINDATE_OF_GOO)) as age,EXTRACT(YEAR from AGE(a.dos,NOW())) as remainingage,e.ddo_code,e.off_en  FROM emp_mast a,g_spc b,g_post c,"
                    + "g_department d,g_office e WHERE  a.cur_spc=b.spc AND b.dept_code=d.department_code AND  "
                    + " b.gpc=c.post_code AND a.cur_off_code=e.off_code AND emp_id=?");

            ps.setString(1, hrmsid);
            rs = ps.executeQuery();
            if (rs.next()) {
                String Loanee_name = rs.getString("fullname");

                String Loanee_des = rs.getString("post");
                String loanee_name_des = Loanee_name + "<br/>" + Loanee_des;
                loanvalue.setEmpName(loanee_name_des);

                String loanee_basicsalary = rs.getString("cur_basic_salary");
                String loanee_gp = rs.getString("gp");

                String basic_gp = "RS " + loanee_basicsalary + "/-" + "<br/>" + "+" + "<br/>" + "RS " + loanee_gp + "/-";
                loanvalue.setBasicsalary(basic_gp);
                if (rs.getString("is_regular").equals("Y")) {
                    loanvalue.setJobType("Perm.");
                } else {
                    loanvalue.setJobType("Temp.");
                }

                loanvalue.setGpfno(rs.getString("gpf_no"));

                loanvalue.setEmpdob(rs.getString("dob"));
                loanvalue.setOffaddress(rs.getString("department_name"));
                loanvalue.setName(Loanee_name);

            }
            ps = con.prepareStatement("SELECT a.* FROM hw_emp_loan a WHERE loan_code=?");
            ps.setInt(1, loanid);
            rs = ps.executeQuery();
            if (rs.next()) {
                loanvalue.setInstalments(rs.getInt("no_installment"));

                Double amount_adv = rs.getDouble("amount_adv");
                loanvalue.setAmounpretadv(Math.round(amount_adv) + "/-");
                Double amount_inst = amount_adv / rs.getInt("no_installment");

                String amt = "RS " + Math.round(amount_inst) + "/-";
                loanvalue.setAmountadv(amt);
                loanvalue.setPreAdvPur(rs.getString("motorcar_cycle_moped"));
                loanvalue.setInstalments(rs.getInt("no_installment"));
                loanvalue.setLetterNo(rs.getString("letterno"));
                loanvalue.setLetterDate(rs.getString("letterdate"));
                loanvalue.setLetterformName(rs.getString("letterfrom"));
                loanvalue.setLetterformdesignation(rs.getString("fromdesignation"));
                loanvalue.setLetterto(rs.getString("letterto"));
                loanvalue.setMemoNo(rs.getString("memono"));
                loanvalue.setMemoDate(rs.getString("memodate"));

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }

        return loanvalue;
    }

    @Override
    public void saveLoansaction(LoanForm lform) {
        Connection con = null;
        Calendar now = Calendar.getInstance();
        Date loanapplyDate = now.getTime();

        PreparedStatement ps = null;
        ResultSet rs = null;
        Date dt1 = null;
        String pending_at = null;
        String pending_spc = null;
        int taskid = lform.getTaskid();
        int loanid = lform.getLoanId();

        String letterno = lform.getLetterNo();
        String letterdate = lform.getLetterDate();
        String letterform = lform.getLetterformName();
        String letterto = lform.getLetterto();
        String letterdesi = lform.getLetterformdesignation();
        String memono = lform.getMemoNo();
        String memodate = lform.getMemoDate();

        MultipartFile loanAttch = lform.getFile_att();
        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("UPDATE hw_emp_loan SET letterno=?,letterdate=?,letterfrom=?,fromdesignation=?,letterto=?,memono=?,memodate=? WHERE taskid=? AND loan_code=?");
            ps.setString(1, letterno);
            ps.setString(2, letterdate);
            ps.setString(3, letterform);
            ps.setString(4, letterdesi);
            ps.setString(5, letterto);
            ps.setString(6, memono);
            ps.setString(7, memodate);
            ps.setInt(8, taskid);
            ps.setInt(9, loanid);
            int stsTask = ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void downloadLoanAttachment(HttpServletResponse response, String filepath, String loanid) throws IOException {

        Connection con = null;

        Statement stmt = null;
        ResultSet rs = null;
        int BUFFER_LENGTH = 4096;
        OutputStream out = response.getOutputStream();
        try {
            con = dataSource.getConnection();

            stmt = con.createStatement();
            String sql = "SELECT * FROM HW_EMP_LOAN WHERE LOAN_CODE='" + loanid + "'";
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                File f = null;
                String dirpath = filepath + "/" + rs.getString("DISK_FILE_NAME");
                
                f = new File(dirpath);
                if (f.exists()) {
                    String originalFilename = rs.getString("ORIGINAL_FILENAME");
                    String filetype = rs.getString("FILE_TYPE");

                    response.setContentLength((int) f.length());
                    FileInputStream is = new FileInputStream(f);

                    response.setContentType(filetype);
                    response.setHeader("Content-Disposition", "attachment; filename=\"" + originalFilename + "\"");

                    byte[] bytes = new byte[BUFFER_LENGTH];
                    int read = 0;
                    while ((read = is.read(bytes, 0, BUFFER_LENGTH)) != -1) {
                        out.write(bytes, 0, read);
                    }
                    is.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, stmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    public void savereapplyLoanData(LoanForm lform, String empid, String filepath) {
        Connection con = null;
        Calendar now = Calendar.getInstance();
        Date loanapplyDate = now.getTime();

        PreparedStatement ps = null;
        ResultSet rs = null;
        Date dt1 = null;
        Date dt2 = null;
        Date dt3 = null;

        DateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        MultipartFile loanAttch = lform.getFile_att();
        try {
            con = this.dataSource.getConnection();
            String forwardId = lform.getForwardtoHrmsid();
            String hidSpc = lform.getHidSPC();
            
            double antprice = Double.parseDouble(lform.getAntprice());
            String session_empId = empid;
            String designation = lform.getDesignation();
            double basicsalary = Double.parseDouble(lform.getBasicsalary());
            double netsalary = Double.parseDouble(lform.getNetsalary());
            String hidOffCode = lform.getHidOffCode();
            String hidOffName = lform.getHidOffName();

            String purtype = lform.getPurtype();
            String amountadv = lform.getAmountadv();

            int instalments = lform.getInstalments();
            String previousAvail = lform.getPreviousAvail();
            String PreAdvPur = lform.getPreAdvPur();
            String amounpretadv = lform.getAmounpretadv();

            String dateofdrawal = lform.getDateofdrawal();
            String intpaidfull = lform.getIntpaidfull();
            String amountstanding = lform.getAmountstanding();

            String officerleave = lform.getOfficerleave();
            String datecommleave = lform.getDatecommleave();
            String dateexpireleave = lform.getDateexpireleave();
            String forwardto = lform.getForwardto();
            String loanapplyfor = lform.getLoanapplyfor();
            int loanvalue = 25;
            String initiatedSpc = lform.getEmpSPC();
            int taskid = lform.getTaskid();

            /**
             * **********************************************************************************
             */
            String strTaskmaster = "UPDATE   task_master SET status_id=?,pending_at=?,apply_to=?,pending_spc=?,apply_to_spc=?,initiated_on=?,note=? WHERE task_id=?";

            ps = con.prepareStatement(strTaskmaster);

            ps.setInt(1, loanvalue);
            ps.setString(2, forwardId);
            ps.setString(3, forwardId);

            ps.setString(4, hidSpc);
            ps.setString(5, hidSpc);
            ps.setTimestamp(6, new Timestamp(loanapplyDate.getTime()));
            ps.setString(7, "");
            ps.setInt(8, taskid);

            int stsTask = ps.executeUpdate();

            /**
             * *************************************************************************************
             */
            //  dt2 = new Date(datecommleave);
            // dt3 = new Date(dateexpireleave);
            
            if (stsTask == 1) {

                String str = "UPDATE hw_emp_loan SET basic_salary=?,net_salary=?,ant_price=?,purchase_type=?,amount_adv=?,no_installment=?,loan_availed=?  ";
                str = str + ",date_drawal_adv=?,availed_amt_adv=?,principal_paid=?,loan_type=?,motorcar_cycle_moped=?,amount_standing=?,officer_leave=?,date_commencement=?,date_expire=?,forward_to=?";
                str = str + ",loan_status=?,loan_apply_date=?,comments=? WHERE loan_code=? ";
                ps = con.prepareStatement(str);

                ps.setDouble(1, basicsalary);
                ps.setDouble(2, netsalary);
                ps.setDouble(3, antprice);
                ps.setString(4, purtype);

                if (amountadv != null && !amountadv.equals("")) {
                    ps.setDouble(5, Double.parseDouble(amountadv));
                } else {
                    ps.setDouble(5, 0);
                }
                ps.setInt(6, instalments);
                ps.setString(7, previousAvail);
                if (lform.getDateofdrawal() != null && !lform.getDateofdrawal().equals("")) {
                    ps.setTimestamp(8, new Timestamp(sdf.parse(lform.getDateofdrawal()).getTime()));
                } else {
                    ps.setTimestamp(8, null);
                }
                if (amounpretadv != null && !amounpretadv.equals("")) {
                    ps.setDouble(9, Double.parseDouble(amounpretadv));
                } else {
                    ps.setDouble(9, 0);
                }
                ps.setString(10, intpaidfull);
                ps.setString(11, loanapplyfor);
                ps.setString(12, PreAdvPur);

                if (amountstanding != null && !amountstanding.equals("")) {
                    ps.setDouble(13, Double.parseDouble(amountstanding));
                } else {
                    ps.setDouble(13, 0);
                }
                ps.setString(14, officerleave);
                if (lform.getDatecommleave() != null && !lform.getDatecommleave().equals("")) {
                    ps.setTimestamp(15, new Timestamp(sdf.parse(lform.getDatecommleave()).getTime()));
                } else {
                    ps.setTimestamp(15, null);
                }
                if (lform.getDateexpireleave() != null && !lform.getDateexpireleave().equals("")) {
                    ps.setTimestamp(16, new Timestamp(sdf.parse(lform.getDateexpireleave()).getTime()));
                } else {
                    ps.setTimestamp(16, null);
                }
                ps.setString(17, hidSpc);
                ps.setInt(18, loanvalue);
                ps.setTimestamp(19, new Timestamp(loanapplyDate.getTime()));
                ps.setString(20, "");
                int loanid = lform.getLoanId();
                ps.setInt(21, loanid);
                int sts = ps.executeUpdate();

                if (sts > 0) {
                    InputStream inputStream = null;
                    OutputStream outputStream = null;
                    String filename = "";
                    if (loanAttch != null && !loanAttch.isEmpty()) {
                        long time = System.currentTimeMillis();
                        filename = session_empId + "_" + taskid + "_" + time;

                        ps = con.prepareStatement("UPDATE hw_emp_loan SET original_filename=?,disk_file_name=?,file_type=? WHERE TASKID=?");
                        ps.setString(1, loanAttch.getOriginalFilename());
                        ps.setString(2, filename);
                        ps.setString(3, loanAttch.getContentType());
                        ps.setInt(4, taskid);
                        ps.executeUpdate();

                        String dirpath = filepath + "/";
                        File newfile = new File(dirpath);
                        if (!newfile.exists()) {
                            newfile.mkdirs();
                        }

                        outputStream = new FileOutputStream(dirpath + filename);
                        int read = 0;
                        byte[] bytes = new byte[1024];
                        inputStream = loanAttch.getInputStream();
                        while ((read = inputStream.read(bytes)) != -1) {
                            outputStream.write(bytes, 0, read);
                        }
                    }
                }
            }

            /* ps = con.prepareStatement("SELECT a.emp_id,a.f_name  FROM emp_mast a,g_spc b WHERE a.cur_spc=b.spc AND b.spc=?");
             ps.setString(1, hidSpc);
             rs = ps.executeQuery();
             String applyToHrmsid=rs.getString("emp_id");*/
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void deleteLoanAttch(int loanid, String filepath) {

        Connection con = null;

        Statement stmt = null;
        ResultSet rs = null;

        PreparedStatement pst = null;

        boolean isDeleted = false;
        try {
            con = dataSource.getConnection();
            stmt = con.createStatement();
            String sql = "SELECT original_filename,disk_file_name FROM HW_EMP_LOAN WHERE LOAN_CODE='" + loanid + "'";
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                File f = null;
                String dirpath = filepath + "/" + rs.getString("DISK_FILE_NAME");
                
                f = new File(dirpath);
                if (f.exists()) {
                    
                    isDeleted = f.delete();
                }
            }
            if (isDeleted) {
                pst = con.prepareStatement("UPDATE HW_EMP_LOAN SET original_filename='',disk_file_name='',file_type='' WHERE LOAN_CODE='" + loanid + "'");
                pst.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, stmt);
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        //return retval2;
    }

    @Override
    public LoanGPFForm GPFEmpDetails(String hrmsid) {
        LoanGPFForm loanvalue = new LoanGPFForm();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Calendar now = Calendar.getInstance();
        int cmonth = now.get(Calendar.MONTH);
        int cyear = now.get(Calendar.YEAR);
        int previousYear = cyear;
        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("SELECT a.gpf_no,a.gp,a.emp_id,ARRAY_TO_STRING(ARRAY[INITIALS, a.F_NAME, a.M_NAME,"
                    + "a.L_NAME], ' ') fullname,a.cur_basic_salary,to_char(a.JOINDATE_OF_GOO, 'DD-Mon-YYYY') as doj,"
                    + "to_char(a.dos, 'DD-Mon-YYYY') as dos,b.spn,b.spc,b.gpc,c.post,d.department_name,"
                    + "EXTRACT(YEAR from AGE(NOW(), a.JOINDATE_OF_GOO)) as age,EXTRACT(YEAR from AGE(a.dos,NOW())) as remainingage,e.ddo_code,e.off_en  FROM emp_mast a,g_spc b,g_post c,"
                    + "g_department d,g_office e WHERE  a.cur_spc=b.spc AND b.dept_code=d.department_code AND  "
                    + " b.gpc=c.post_code AND a.cur_off_code=e.off_code AND emp_id=?");
            ps.setString(1, hrmsid);
            rs = ps.executeQuery();
            if (rs.next()) {
                loanvalue.setEmpName(rs.getString("fullname"));
                String gpfno = rs.getString("gpf_no");
                loanvalue.setEmpSPC(rs.getString("spc"));
                String deptname = rs.getString("department_name");
                String accountNo = gpfno + " " + deptname;
                loanvalue.setGpfno(accountNo);
                int serviceYear = rs.getInt("age");
                int remainingAge = rs.getInt("remainingage");
                String gpftypestatus = null;
                if (serviceYear >= 20 || remainingAge < 10) {
                    gpftypestatus = "NON-REFUNDABLE";
                } else {
                    gpftypestatus = "REFUNDABLE";
                }
                loanvalue.setGpftype(gpftypestatus);
                String basicsalary_gp = rs.getString("cur_basic_salary") + "+ GP " + rs.getString("gp");
                loanvalue.setDesignation(rs.getString("post"));
                loanvalue.setPay(basicsalary_gp);
                loanvalue.setDoj(rs.getString("doj"));
                loanvalue.setSupperannuation(rs.getString("dos"));
                
                if (cmonth <= 3) {
                    previousYear = cyear - 1;
                    //cyear = previousYear;

                }
                if (cmonth == 0) {
                    cmonth = 12;
                }
                loanvalue.setCyear(cyear);

                loanvalue.setPreviousYear(previousYear);
                loanvalue.setCmonth(cmonth);
                String ddo_code = rs.getString("ddo_code");
                String ddo_office = rs.getString("off_en");
                String ddooffice = ddo_office + "(" + ddo_code + ")";
                loanvalue.setAccountOfficer(ddooffice);
                loanvalue.setDdocode(ddo_code);
                

            }
            ps = con.prepareStatement("SELECT SUM(ad_amt) as total_subscription FROM aq_dtls WHERE ad_code='GPF' AND  emp_code='" + hrmsid + "' AND (p_month BETWEEN 4 AND '" + cmonth + "' AND (p_year  BETWEEN  '" + previousYear + "' AND  '" + cyear + "')) ");
            
            rs = ps.executeQuery();
            String total_subscription = null;
            if (rs.next()) {
                total_subscription = rs.getString("total_subscription");
            }
            loanvalue.setCreditAmount(total_subscription);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return loanvalue;
    }

    public void savegpfLoanData(LoanGPFForm lform, String empid, String filepath) {
        Connection con = null;
        Calendar now = Calendar.getInstance();
        Date loanapplyDate = now.getTime();

        PreparedStatement ps = null;
        ResultSet rs = null;
        Date dt1 = null;
        Date dt2 = null;
        Date dt3 = null;

        DateFormat cdate = new SimpleDateFormat("dd-MMM-yyyy");
        MultipartFile loanAttch = lform.getFile_att();
        try {
            con = this.dataSource.getConnection();
            String forwardId = lform.getForwardtoHrmsid();
            String hidSpc = lform.getHidSPC();
            int loanvalue = 35;
            String initiatedSpc = lform.getEmpSPC();
            // String loanapplyfor=lform.getLoanapplyfor();
            String session_empId = empid;

            String cbalnace = lform.getClosingbalance();
            String camount = lform.getCreditAmount();
            String cform = lform.getCreditForm();
            String cto = lform.getCreditTo();
            String ramount = lform.getRefund();
            String wfrom = lform.getWithdrawfrom();
            String wto = lform.getWithdrawto();
            String wamount = lform.getWithdrawalAmount();
            String nbalance = lform.getNetbalance();
            String amountrequired = lform.getWithdrawalreq();
            String purpose = lform.getPurpose();
            String rulegpf_request = lform.getRequestcovered();
            String ddo = lform.getAccountOfficer();
            String gppftype = lform.getGpftype();
            String gppftype_value = gppftype.replace(",", "");
            String samepurpose = lform.getWithdrawaltaken();
            String bcredit = lform.getBalanceCredit();

            String strTaskmaster = "INSERT INTO task_master (process_id "
                    + ",initiated_by "
                    + ",status_id "
                    + ",pending_at "
                    + ",apply_to "
                    + ",initiated_spc "
                    + ",pending_spc "
                    + ",apply_to_spc "
                    + ",initiated_on "
                    + ") values(?,?,?,?,?,?,?,?,?)";
            // ps = con.prepareStatement(strTaskmaster);
            ps = con.prepareStatement(strTaskmaster, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, 8);
            ps.setString(2, session_empId);
            ps.setInt(3, loanvalue);
            ps.setString(4, forwardId);
            ps.setString(5, forwardId);
            ps.setString(6, initiatedSpc);
            ps.setString(7, hidSpc);
            ps.setString(8, hidSpc);
            ps.setTimestamp(9, new Timestamp(loanapplyDate.getTime()));

            int stsTask = ps.executeUpdate();
            rs = ps.getGeneratedKeys();
            rs.next();
            int taskId = rs.getInt("TASK_ID");

            if (stsTask == 1) {
                String str = "INSERT INTO hw_emp_gpf_loan(taskid "
                        + ",emp_id "
                        + ",closing_balance"
                        + ",credit_amount"
                        + ",credit_from"
                        + ",credit_to"
                        + ",refund_amount"
                        + ",withdrawal_from"
                        + ",withdrawal_to"
                        + ",withdrawal_amount"
                        + ",net_balance"
                        + ",amount_required"
                        + ",purpose"
                        + ",rule_gpf"
                        + ",same_purpse_details"
                        + ",ddo_code"
                        + ",forward_to"
                        + ",loan_apply_date"
                        + ",loan_status,gpftype,balancecredit) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                ps = con.prepareStatement(str);
                ps.setInt(1, taskId);
                ps.setString(2, session_empId);
                if (cbalnace != null && !cbalnace.equals("")) {
                    ps.setDouble(3, Double.parseDouble(cbalnace));
                } else {
                    ps.setDouble(3, 0);
                }
                if (camount != null && !camount.equals("")) {
                    ps.setDouble(4, Double.parseDouble(camount));
                } else {
                    ps.setDouble(4, 0);
                }
                ps.setString(5, cform);
                ps.setString(6, cto);
                if (ramount != null && !ramount.equals("")) {
                    ps.setDouble(7, Double.parseDouble(ramount));
                } else {
                    ps.setDouble(7, 0);
                }
                ps.setString(8, wfrom);
                ps.setString(9, wto);
                if (wamount != null && !wamount.equals("")) {
                    ps.setDouble(10, Double.parseDouble(wamount));
                } else {
                    ps.setDouble(10, 0);
                }
                if (nbalance != null && !nbalance.equals("")) {
                    ps.setDouble(11, Double.parseDouble(nbalance));
                } else {
                    ps.setDouble(11, 0);
                }
                if (amountrequired != null && !amountrequired.equals("")) {
                    ps.setDouble(12, Double.parseDouble(amountrequired));
                } else {
                    ps.setDouble(12, 0);
                }
                ps.setString(13, purpose);
                ps.setString(14, rulegpf_request);
                ps.setString(15, samepurpose);
                ps.setString(16, ddo);
                ps.setString(17, hidSpc);
                ps.setTimestamp(18, new Timestamp(loanapplyDate.getTime()));
                ps.setInt(19, loanvalue);
                ps.setString(20, gppftype_value);
                if (bcredit != null && !bcredit.equals("")) {
                    ps.setDouble(21, Double.parseDouble(bcredit));
                } else {
                    ps.setDouble(21, 0);
                }
                int sts = ps.executeUpdate();
                if (sts > 0) {
                    InputStream inputStream = null;
                    OutputStream outputStream = null;
                    String filename = "";
                    if (loanAttch != null && !loanAttch.isEmpty()) {
                        long time = System.currentTimeMillis();
                        filename = session_empId + "_" + taskId + "_" + time;

                        ps = con.prepareStatement("UPDATE hw_emp_gpf_loan SET original_filename=?,disk_file_name=?,file_type=? WHERE TASKID=?");
                        ps.setString(1, loanAttch.getOriginalFilename());
                        ps.setString(2, filename);
                        ps.setString(3, loanAttch.getContentType());
                        ps.setInt(4, taskId);
                        ps.executeUpdate();

                        String dirpath = filepath + "/";
                        File newfile = new File(dirpath);
                        if (!newfile.exists()) {
                            newfile.mkdirs();
                        }

                        outputStream = new FileOutputStream(dirpath + filename);
                        int read = 0;
                        byte[] bytes = new byte[1024];
                        inputStream = loanAttch.getInputStream();
                        while ((read = inputStream.read(bytes)) != -1) {
                            outputStream.write(bytes, 0, read);
                        }
                    }

                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public LoanGPFForm getgpfLoanDetails(int taskid, String hrmsid) {

        LoanGPFForm loanvalue = new LoanGPFForm();
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement pstask = null;
        PreparedStatement psapply = null;
        ResultSet rs = null;
        ResultSet rstask = null;
        ResultSet rsapply = null;
        Calendar now = Calendar.getInstance();
        int cmonth = now.get(Calendar.MONTH);
        int cyear = now.get(Calendar.YEAR);
        int previousYear = cyear;
        String empId = null;
        int loanid = 0;
        int statusId = 0;

        try {
            con = this.dataSource.getConnection();
            pstask = con.prepareStatement("SELECT a.*,b.* FROM hw_emp_gpf_loan a,task_master b WHERE a.taskid=b.task_id AND a.taskid='" + taskid + "' ");
            rstask = pstask.executeQuery();
            if (rstask.next()) {
                empId = rstask.getString("emp_id");
                loanvalue.setEmpId(rstask.getString("emp_id"));
                loanvalue.setClosingbalance(rstask.getString("closing_balance"));
                loanvalue.setBalanceCredit(rstask.getString("balancecredit"));
                loanvalue.setCreditForm(rstask.getString("credit_from"));
                loanvalue.setCreditTo(rstask.getString("credit_to"));
                loanvalue.setCreditAmount(rstask.getString("credit_amount"));
                loanvalue.setRefund(rstask.getString("refund_amount"));
                loanvalue.setWithdrawfrom(rstask.getString("withdrawal_from"));
                loanvalue.setWithdrawto(rstask.getString("withdrawal_to"));
                loanvalue.setWithdrawalAmount(rstask.getString("withdrawal_amount"));
                loanvalue.setNetbalance(rstask.getString("net_balance"));
                loanvalue.setWithdrawalreq(rstask.getString("amount_required"));
                loanvalue.setPurpose(rstask.getString("purpose"));
                loanvalue.setRequestcovered(rstask.getString("rule_gpf"));
                loanvalue.setWithdrawaltaken(rstask.getString("same_purpse_details"));
                loanvalue.setApprovedBy(rstask.getString("apply_to"));
                loanvalue.setApprovedSpc(rstask.getString("pending_spc"));
                loanvalue.setLoanId(rstask.getInt("loan_gpf_id"));
                loanvalue.setTaskid(rstask.getInt("taskid"));
                loanvalue.setLoancomments(rstask.getString("comments"));
                loanvalue.setStatusId(rstask.getInt("status_id"));
                loanvalue.setGpftype(rstask.getString("loan_gpf_id"));
                statusId = rstask.getInt("loan_status");
                loanid = rstask.getInt("loan_gpf_id");

            }

            ps = con.prepareStatement("SELECT a.gpf_no,a.gp,a.emp_id,ARRAY_TO_STRING(ARRAY[INITIALS, a.F_NAME, a.M_NAME,"
                    + "a.L_NAME], ' ') fullname,a.cur_basic_salary,to_char(a.JOINDATE_OF_GOO, 'DD-Mon-YYYY') as doj,"
                    + "to_char(a.dos, 'DD-Mon-YYYY') as dos,b.spn,b.spc,b.gpc,c.post,d.department_name,"
                    + "EXTRACT(YEAR from AGE(NOW(), a.JOINDATE_OF_GOO)) as age,EXTRACT(YEAR from AGE(a.dos,NOW())) as remainingage,e.ddo_code,e.off_en  FROM emp_mast a,g_spc b,g_post c,"
                    + "g_department d,g_office e WHERE  a.cur_spc=b.spc AND b.dept_code=d.department_code AND  "
                    + " b.gpc=c.post_code AND a.cur_off_code=e.off_code AND emp_id=?");
            ps.setString(1, empId);
            rs = ps.executeQuery();
            if (rs.next()) {
                loanvalue.setEmpName(rs.getString("fullname"));
                String gpfno = rs.getString("gpf_no");
                loanvalue.setEmpSPC(rs.getString("spc"));
                String deptname = rs.getString("department_name");
                String accountNo = gpfno + " (" + deptname + ")";
                loanvalue.setGpfno(accountNo);
                int serviceYear = rs.getInt("age");
                int remainingAge = rs.getInt("remainingage");

                String basicsalary_gp = rs.getString("cur_basic_salary") + "+ GP " + rs.getString("gp");
                loanvalue.setDesignation(rs.getString("post"));
                loanvalue.setPay(basicsalary_gp);
                loanvalue.setDoj(rs.getString("doj"));
                loanvalue.setSupperannuation(rs.getString("dos"));

                if (cmonth < 4) {
                    previousYear = cyear - 1;
                    cyear = previousYear;

                }
                if (cmonth == 0) {
                    cmonth = 12;
                }
                loanvalue.setCyear(cyear);

                loanvalue.setPreviousYear(previousYear);
                loanvalue.setCmonth(cmonth);
                String ddo_code = rs.getString("ddo_code");
                String ddo_office = rs.getString("off_en");
                String ddooffice = ddo_office + "(" + ddo_code + ")";
                loanvalue.setAccountOfficer(ddooffice);
                loanvalue.setDdocode(ddo_code);
                if (rstask.getString("disk_file_name") != null && !rstask.getString("disk_file_name").equals("")) {
                    loanvalue.setDiskFileName(rstask.getString("disk_file_name"));
                } else {
                    loanvalue.setDiskFileName("");
                }

            }
            if (statusId == 99) {

                pstask = con.prepareStatement("SELECT a.*,to_char(a.sanction_date, 'DD-Mon-YYYY') as fsanction_date,to_char(a.recovery_sdate, 'DD-Mon-YYYY') as frecovery_sdate FROM hw_loan_sanction_details a WHERE  a.taskid=? AND a.apply_loanid=? ");
                pstask.setInt(1, taskid);
                pstask.setInt(2, loanid);
                rstask = pstask.executeQuery();
                if (rstask.next()) {
                    loanvalue.setLetterNo(rstask.getString("sanction_no"));
                    loanvalue.setLetterDate(rstask.getString("fsanction_date"));
                    loanvalue.setSamount(rstask.getString("sanction_amount"));
                    loanvalue.setReleaseAmount(rstask.getString("release_amount"));
                    loanvalue.setEmiPrincipal(rstask.getInt("prinicipal_installment"));
                    loanvalue.setPrincipalAmount(rstask.getString("prinicipal_installment_amt"));
                    loanvalue.setTotalInterestamount(rstask.getString("interestamount"));
                    loanvalue.setEmiInterest(rstask.getInt("interest_installment"));
                    loanvalue.setInterestAmount(rstask.getString("interest_installment_amt"));
                    loanvalue.setInterestType(rstask.getString("interesttype"));
                    loanvalue.setRateInterest(rstask.getString("rate_interest"));
                    loanvalue.setLastInstallment(rstask.getString("last_installment_amt"));
                    loanvalue.setPenalRate(rstask.getString("penal_rate_interest"));
                    loanvalue.setMoratoriumRequired(rstask.getString("moratorium_required"));
                    loanvalue.setMoratoriumPeriod(rstask.getString("moratorium_period"));
                    loanvalue.setInsuranceFlag(rstask.getString("insurance_flag"));
                    loanvalue.setRecDate(rstask.getString("frecovery_sdate"));
                    loanvalue.setChatofAccount(rstask.getString("chat_account"));
                    loanvalue.setBillid(rstask.getString("bill_id"));
                    loanvalue.setDemandNo(rstask.getString("demand_no"));
                    loanvalue.setMajorhead(rstask.getString("major_head"));

                    // loanvalue.se
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return loanvalue;

    }

    public void savegpfApproveLoanData(LoanGPFForm lform, String empid) {
        Connection con = null;
        Calendar now = Calendar.getInstance();
        Date loanapplyDate = now.getTime();

        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        Date dt1 = null;
        String pending_at = null;
        String pending_spc = null;
        int taskid = lform.getTaskid();
        int loanid = lform.getLoanId();
        int loanstatus = lform.getLoan_status();
        String Comments = lform.getLoancomments();
        String approvedby = lform.getApprovedBy();
        String approvedspc = lform.getApprovedSpc();

        DateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        String gpfnumber = "";
        String gpfSeries = "";
        MultipartFile loanAttch = lform.getFile_att();
        try {
            con = this.dataSource.getConnection();
            if (loanstatus == 45 || loanstatus == 47 || loanstatus == 99) {
                String forwardId = lform.getForwardtoHrmsid();
                String hidSpc = lform.getHidSPC();
                ps = con.prepareStatement(" SELECT * FROM task_master WHERE  task_id='" + taskid + "'");
                rs = ps.executeQuery();
                while (rs.next()) {
                    pending_at = (rs.getString("pending_at"));
                    pending_spc = (rs.getString("pending_spc"));
                }

                ps = con.prepareStatement("UPDATE task_master SET status_id=?,pending_at=?,note=?,apply_to=?,pending_spc=?,apply_to_spc=? WHERE task_id=?");
                ps.setInt(1, loanstatus);
                ps.setString(2, forwardId);
                ps.setString(3, Comments);
                ps.setString(4, forwardId);
                ps.setString(5, hidSpc);
                ps.setString(6, hidSpc);
                ps.setInt(7, taskid);
                int stsTask = ps.executeUpdate();
                if (stsTask == 1) {
                    String str = "INSERT INTO workflow_log(task_id "
                            + ",task_action_date "
                            + ",action_taken_by"
                            + ",spc_ontime"
                            + ",task_status_id"
                            + ",note"
                            + ",forward_to"
                            + ",forwarded_spc"
                           
                            + ",ref_id"
                            + ") values(?,?,?,?,?,?,?,?,?)";
                    ps = con.prepareStatement(str);
                    ps.setInt(1, taskid);
                    ps.setTimestamp(2, new Timestamp(loanapplyDate.getTime()));
                    ps.setString(3, pending_at);
                    ps.setString(4, pending_spc);
                    ps.setInt(5, loanstatus);
                    ps.setString(6, Comments);
                    ps.setString(7, forwardId);
                    ps.setString(8, hidSpc);
                    ps.setInt(9, loanid);
                   // int wfcode = CommonFunctions.getMaxCodeInteger("workflow_log", "log_id", con);
                  //  ps.setInt(9, wfcode);
                    ps.executeUpdate();

                    ps = con.prepareStatement("UPDATE hw_emp_gpf_loan SET loan_status=?,comments=? WHERE taskid=? AND loan_gpf_id=?");
                    ps.setInt(1, loanstatus);
                    ps.setString(2, Comments);
                    ps.setInt(3, taskid);
                    ps.setInt(4, loanid);
                    ps.executeUpdate();

                    if (loanstatus == 45) {
                        ps = con.prepareStatement("SELECT * FROM hw_emp_gpf_loan WHERE  taskid=? AND loan_gpf_id=? LIMIT 1");
                        ps.setInt(1, taskid);
                        ps.setInt(2, loanid);
                        rs = ps.executeQuery();
                        if (rs.next()) {
                            Date approverdate = now.getTime();
                            String loanApplyId = rs.getString("emp_id");
                            String gpftype = rs.getString("gpftype");
                            gpftype = gpftype.trim();
                            String loanshortcode = "";
                            String chatofaccount = "";
                            String majorhead = "";
                            String billid = "";
                            String loanType = "GPF";
                            if (gpftype.equals("REFUNDABLE")) {
                                loanshortcode = "GA";
                                chatofaccount = "057610002040825480570001110";
                                majorhead = "7610";
                                billid = "11";
                                loanType = "GPF-REFUNDABLE";
                            }
                            if (gpftype.equals("NON-REFUNDABLE")) {
                                loanshortcode = "NGA";
                                chatofaccount = "057610002020020480010001110";
                                majorhead = "7610";
                                billid = "11";
                                loanType = "GPF-NON-REFUNDABLE";
                            }

                            Users ue = getLoanEmpDetail(loanApplyId);
                            String ifmsLoanId = loanshortcode + ue.getGpfno();

                            String str1 = "INSERT INTO hw_loan_sanction_details(taskid "
                                    + ",apply_loanId "
                                    + ",loan_type"
                                    + ",loanee_id"
                                    + ",approver_id"
                                    + ",approved_date"
                                    + ",gpfNo"
                                    + ",accountNo"
                                    + ",pranNo"
                                    + ",name"
                                    + ",designation"
                                    + ",office_name"
                                    + ",dob"
                                    + ",dos"
                                    + ",basic_pay"
                                    + ",gp"
                                    + ",empType"
                                    + ",address"
                                    + ",gpf_series"
                                    + ",gpf_accountno"
                                    + ",ifms_loanid"
                                    + ",demand_no"
                                    + ",major_head"
                                    + ",chat_account"
                                    + ",bill_id"
                                    + ",ddo_code"
                                    + ",bank_account"
                                    + ",ifsc_code"
                                    + ",mobile"
                                    + ") values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                            ps1 = con.prepareStatement(str1);
                            ps1.setInt(1, taskid);
                            ps1.setInt(2, loanid);
                            ps1.setString(3, loanType);
                            ps1.setString(4, rs.getString("emp_id"));
                            ps1.setString(5, empid);
                            ps1.setTimestamp(6, new Timestamp(approverdate.getTime()));
                            if (ue.getAcctType().equals("GPF")) {
                                gpfnumber = getempNumberFromGPF(ue.getGpfno());
                                gpfSeries = CommonFunctions.getGPFSeries(ue.getGpfno());
                                ps1.setString(7, ue.getGpfno());
                                ps1.setString(8, ue.getAcctType());
                                ps1.setString(9, "");

                            } else {
                                ps1.setString(7, "");
                                ps1.setString(8, ue.getAcctType());
                                ps1.setString(9, ue.getGpfno());
                                gpfnumber = ue.getGpfno();

                            }

                            ps1.setString(10, ue.getFullName());
                            ps1.setString(11, ue.getPostname());
                            ps1.setString(12, ue.getOffname());
                            ps1.setTimestamp(13, new Timestamp(sdf.parse(ue.getEmpDob()).getTime()));
                            ps1.setTimestamp(14, new Timestamp(sdf.parse(ue.getEmpDos()).getTime()));
                            ps1.setDouble(15, Double.parseDouble(ue.getBasicsalry()));
                            ps1.setDouble(16, Double.parseDouble(ue.getGp()));
                            // ps1.setString(15, rs.getString("cur_basic_salary"));
                            // ps1.setString(16, rs.getString("gp"));
                            if (ue.getIsRegular().equals("Y")) {
                                ps1.setString(17, "Permanent");
                                //	loanvalue.setJobType("Permanent");
                            } else {
                                ps1.setString(17, "Temporary");
                                //loanvalue.setJobType("Temporary");
                            }
                            ps1.setString(18, ue.getAddress());
                            ps1.setString(19, gpfSeries);
                            ps1.setString(20, gpfnumber);
                            ps1.setString(21, ifmsLoanId);
                            ps1.setString(22, "05");
                            ps1.setString(23, majorhead);
                            ps1.setString(24, chatofaccount);
                            ps1.setString(25, billid);
                            ps1.setString(26, ue.getDdoCode());
                            ps1.setString(27, ue.getBankAccNo());
                            ps1.setString(28, ue.getIfmsCode());
                            ps1.setString(29, ue.getMobile());
                            ps1.executeUpdate();
                        }
//  System.exit(0); 

                    }

                    if (loanstatus == 99) {
                        Date entryDate = now.getTime();
                        ps1 = con.prepareStatement("UPDATE hw_loan_sanction_details SET loan_sanction_login_id=?,sanction_entry_date=?,interesttype=?,sanction_amount=?,release_amount=?,prinicipal_installment=?,prinicipal_installment_amt=?,interest_installment=?,interest_installment_amt=?,rate_interest=?,last_installment_amt=?,penal_rate_interest=?,moratorium_required=?,moratorium_period=?,insurance_flag=?,recovery_sdate=?,interestamount=? WHERE taskid=? AND apply_loanid=?");

                        ps1.setString(1, empid);
                        ps1.setTimestamp(2, new Timestamp(entryDate.getTime()));
                        ps1.setString(3, lform.getInterestType());
                        String sanction_amount = lform.getSamount();
                        String release_amount = lform.getRamount();
                        if (sanction_amount != null && !sanction_amount.equals("")) {
                            ps1.setDouble(4, Double.parseDouble(sanction_amount));
                        } else {
                            ps1.setDouble(4, 0);
                        }
                        if (release_amount != null && !release_amount.equals("")) {
                            ps1.setDouble(5, Double.parseDouble(release_amount));
                        } else {
                            ps1.setDouble(5, 0);
                        }
                        ps1.setInt(6, lform.getEmiPrincipal());
                        String PrincipalAmount = lform.getPrincipalAmount();
                        ps1.setDouble(7, Double.parseDouble(PrincipalAmount));
                        ps1.setInt(8, lform.getEmiInterest());
                        ps1.setDouble(9, Double.parseDouble(lform.getInterestAmount()));
                        ps1.setDouble(10, Double.parseDouble(lform.getRateInterest()));
                        String last_installment_amt = lform.getLastInstallment();
                        if (last_installment_amt != null && !last_installment_amt.equals("")) {
                            ps1.setDouble(11, Double.parseDouble(last_installment_amt));
                        } else {
                            ps1.setDouble(11, 0);
                        }
                        String penal_rate_interest = lform.getPenalRate();
                        if (penal_rate_interest != null && !penal_rate_interest.equals("")) {
                            ps1.setDouble(12, Double.parseDouble(penal_rate_interest));
                        } else {
                            ps1.setDouble(12, 0);
                        }
                        ps1.setString(13, lform.getMoratoriumRequired());
                        ps1.setString(14, lform.getMoratoriumPeriod());
                        ps1.setString(15, lform.getInsuranceFlag());
// String recDate = lform.getRecDate();
                        if (lform.getRecDate() != null && !lform.getRecDate().equals("")) {
                            ps1.setTimestamp(16, new Timestamp(sdf.parse(lform.getRecDate()).getTime()));
                        } else {
                            ps1.setTimestamp(16, null);
                        }

                        ps1.setDouble(17, Double.parseDouble(lform.getTotalInterestamount()));

                        ps1.setInt(18, taskid);
                        ps1.setInt(19, loanid);
                        ps1.executeUpdate();

                    }

                }

            } else {
                ps = con.prepareStatement("UPDATE hw_emp_gpf_loan SET loan_status=?,approved_by=?,approved_spc=?,comments=?,loan_sanction_date=? WHERE taskid=? AND loan_gpf_id=?");
                ps.setInt(1, loanstatus);
                ps.setString(2, approvedby);
                ps.setString(3, approvedspc);
                ps.setString(4, Comments);
                ps.setTimestamp(5, new Timestamp(loanapplyDate.getTime()));
                ps.setInt(6, taskid);
                ps.setInt(7, loanid);
                int stsTask = ps.executeUpdate();

                if (stsTask == 1) {
                    ps = con.prepareStatement("UPDATE task_master SET status_id=?,pending_at=?,note=?,apply_to=?,pending_spc=?,apply_to_spc=? WHERE task_id=?");
                    ps.setInt(1, loanstatus);
                    ps.setString(2, null);
                    ps.setString(3, Comments);
                    ps.setString(4, null);
                    ps.setString(5, null);
                    ps.setString(6, null);
                    ps.setInt(7, taskid);
                    ps.executeUpdate();
                }    

                if (loanstatus == 44) {
                    Date entryDate = now.getTime();
                    ps1 = con.prepareStatement("UPDATE hw_loan_sanction_details SET issue_letter_login_id=?,issue_letter_entry_date=?,sanction_no=?,sanction_date=? WHERE taskid=? AND apply_loanid=?");

                    ps1.setString(1, empid);
                    ps1.setTimestamp(2, new Timestamp(entryDate.getTime()));

                    ps1.setString(3, lform.getLetterNo());
                    if (lform.getLetterDate() != null && !lform.getLetterDate().equals("")) {
                        ps1.setTimestamp(4, new Timestamp(sdf.parse(lform.getLetterDate()).getTime()));
                    } else {
                        ps1.setTimestamp(4, null);
                    }
                    ps1.setInt(5, taskid);
                    ps1.setInt(6, loanid);
                    ps1.executeUpdate();

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    public LoanGPFForm ReplygpfLoan(String option, String hrmsid, int loanid) {

        LoanGPFForm loanvalue = new LoanGPFForm();
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement pstask = null;
        PreparedStatement psapply = null;
        ResultSet rs = null;
        ResultSet rstask = null;
        ResultSet rsapply = null;
        Calendar now = Calendar.getInstance();
        int cmonth = now.get(Calendar.MONTH);
        int cyear = now.get(Calendar.YEAR);
        int previousYear = cyear;

        try {
            con = this.dataSource.getConnection();
            pstask = con.prepareStatement("SELECT a.*,b.* FROM hw_emp_gpf_loan a,task_master b WHERE a.taskid=b.task_id AND a.loan_gpf_id='" + loanid + "' ");
            rstask = pstask.executeQuery();
            if (rstask.next()) {
                loanvalue.setClosingbalance(rstask.getString("closing_balance"));
                loanvalue.setBalanceCredit(rstask.getString("balancecredit"));
                loanvalue.setCreditForm(rstask.getString("credit_from"));
                loanvalue.setCreditTo(rstask.getString("credit_to"));
                loanvalue.setCreditAmount(rstask.getString("credit_amount"));
                loanvalue.setRefund(rstask.getString("refund_amount"));
                loanvalue.setWithdrawfrom(rstask.getString("withdrawal_from"));
                loanvalue.setWithdrawto(rstask.getString("withdrawal_to"));
                loanvalue.setWithdrawalAmount(rstask.getString("withdrawal_amount"));
                loanvalue.setNetbalance(rstask.getString("net_balance"));
                loanvalue.setWithdrawalreq(rstask.getString("amount_required"));
                loanvalue.setPurpose(rstask.getString("purpose"));
                loanvalue.setRequestcovered(rstask.getString("rule_gpf"));
                loanvalue.setWithdrawaltaken(rstask.getString("same_purpse_details"));
                loanvalue.setApprovedBy(rstask.getString("apply_to"));
                loanvalue.setApprovedSpc(rstask.getString("pending_spc"));
                loanvalue.setLoanId(rstask.getInt("loan_gpf_id"));
                loanvalue.setTaskid(rstask.getInt("taskid"));
                loanvalue.setLoancomments(rstask.getString("comments"));
                loanvalue.setGpftype(rstask.getString("gpftype"));

            }

            ps = con.prepareStatement("SELECT a.gpf_no,a.gp,a.emp_id,ARRAY_TO_STRING(ARRAY[INITIALS, a.F_NAME, a.M_NAME,"
                    + "a.L_NAME], ' ') fullname,a.cur_basic_salary,to_char(a.JOINDATE_OF_GOO, 'DD-Mon-YYYY') as doj,"
                    + "to_char(a.dos, 'DD-Mon-YYYY') as dos,b.spn,b.spc,b.gpc,c.post,d.department_name,"
                    + "EXTRACT(YEAR from AGE(NOW(), a.JOINDATE_OF_GOO)) as age,EXTRACT(YEAR from AGE(a.dos,NOW())) as remainingage,e.ddo_code,e.off_en  FROM emp_mast a,g_spc b,g_post c,"
                    + "g_department d,g_office e WHERE  a.cur_spc=b.spc AND b.dept_code=d.department_code AND  "
                    + " b.gpc=c.post_code AND a.cur_off_code=e.off_code AND emp_id=?");
            ps.setString(1, hrmsid);
            rs = ps.executeQuery();
            if (rs.next()) {
                loanvalue.setEmpName(rs.getString("fullname"));
                String gpfno = rs.getString("gpf_no");
                loanvalue.setEmpSPC(rs.getString("spc"));
                String deptname = rs.getString("department_name");
                String accountNo = gpfno + " (" + deptname + ")";
                loanvalue.setGpfno(accountNo);
                int serviceYear = rs.getInt("age");
                int remainingAge = rs.getInt("remainingage");

                String basicsalary_gp = rs.getString("cur_basic_salary") + "+ GP " + rs.getString("gp");
                loanvalue.setDesignation(rs.getString("post"));
                loanvalue.setPay(basicsalary_gp);
                loanvalue.setDoj(rs.getString("doj"));
                loanvalue.setSupperannuation(rs.getString("dos"));

                if (cmonth < 4) {
                    previousYear = cyear - 1;
                    cyear = previousYear;

                }
                if (cmonth == 0) {
                    cmonth = 12;
                }
                loanvalue.setCyear(cyear);

                loanvalue.setPreviousYear(previousYear);
                loanvalue.setCmonth(cmonth);
                String ddo_code = rs.getString("ddo_code");
                String ddo_office = rs.getString("off_en");
                String ddooffice = ddo_office + "(" + ddo_code + ")";
                loanvalue.setAccountOfficer(ddooffice);
                loanvalue.setDdocode(ddo_code);
                if (rstask.getString("disk_file_name") != null && !rstask.getString("disk_file_name").equals("")) {
                    loanvalue.setDiskFileName(rstask.getString("disk_file_name"));
                } else {
                    loanvalue.setDiskFileName("");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return loanvalue;

    }

    @Override
    public LoanGPFForm SactionGPFOrder(int taskid, int loanid) {

        LoanGPFForm loanvalue = new LoanGPFForm();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        loanvalue.setTaskid(taskid);
        loanvalue.setLoanId(loanid);
        String empId = null;
        Calendar now = Calendar.getInstance();
        int cmonth = now.get(Calendar.MONTH);
        int cyear = now.get(Calendar.YEAR);
        int previousYear = cyear;

        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("SELECT a.* FROM hw_emp_gpf_loan a WHERE loan_gpf_id=?");
            ps.setInt(1, loanid);
            rs = ps.executeQuery();
            if (rs.next()) {
                loanvalue.setLetterNo(rs.getString("letterno"));
                loanvalue.setLetterDate(rs.getString("letterdate"));
                loanvalue.setLetterformName(rs.getString("letterfrom"));
                loanvalue.setLetterformdesignation(rs.getString("fromdesignation"));
                loanvalue.setLetterto(rs.getString("letterto"));
                loanvalue.setMemoNo(rs.getString("memono"));
                loanvalue.setMemoDate(rs.getString("memodate"));
                loanvalue.setSanDate(rs.getString("san_date"));
                loanvalue.setSanNo(rs.getString("san_no"));
                loanvalue.setIssueNo(rs.getString("issue_no"));
                loanvalue.setFileno(rs.getString("file_no"));
                loanvalue.setMemoNo(rs.getString("memono"));
                loanvalue.setMemoDate(rs.getString("memodate"));
                empId = rs.getString("emp_id");

                loanvalue.setClosingbalance(rs.getString("closing_balance"));
                loanvalue.setBalanceCredit(rs.getString("balancecredit"));
                loanvalue.setCreditForm(rs.getString("credit_from"));
                loanvalue.setCreditTo(rs.getString("credit_to"));
                loanvalue.setCreditAmount(rs.getString("credit_amount"));
                loanvalue.setRefund(rs.getString("refund_amount"));
                loanvalue.setWithdrawfrom(rs.getString("withdrawal_from"));
                loanvalue.setWithdrawto(rs.getString("withdrawal_to"));
                loanvalue.setWithdrawalAmount(rs.getString("withdrawal_amount"));
                loanvalue.setNetbalance(rs.getString("net_balance"));
                loanvalue.setWithdrawalreq(rs.getString("amount_required"));
                loanvalue.setPurpose(rs.getString("purpose"));
                loanvalue.setRequestcovered(rs.getString("rule_gpf"));
                loanvalue.setWithdrawaltaken(rs.getString("same_purpse_details"));
                loanvalue.setGpftype(rs.getString("gpftype"));

                if (cmonth <= 3) {
                    previousYear = cyear - 1;
                    //cyear = previousYear;

                }
                if (cmonth == 0) {
                    cmonth = 12;
                }
                loanvalue.setCyear(cyear);

            }
            ps = con.prepareStatement("SELECT a.is_regular,a.gpf_no,a.gp,a.emp_id,ARRAY_TO_STRING(ARRAY[INITIALS, a.F_NAME, a.M_NAME,"
                    + "a.L_NAME], ' ') fullname,to_char(a.dob, 'DD-Mon-YYYY') as dob,a.cur_basic_salary,to_char(a.JOINDATE_OF_GOO, 'DD-Mon-YYYY') as doj,"
                    + "to_char(a.dos, 'DD-Mon-YYYY') as dos,b.spn,b.spc,b.gpc,c.post,d.department_name,"
                    + "EXTRACT(YEAR from AGE(NOW(), a.JOINDATE_OF_GOO)) as age,EXTRACT(YEAR from AGE(a.dos,NOW())) as remainingage,e.ddo_code,e.off_en  FROM emp_mast a,g_spc b,g_post c,"
                    + "g_department d,g_office e WHERE  a.cur_spc=b.spc AND b.dept_code=d.department_code AND  "
                    + " b.gpc=c.post_code AND a.cur_off_code=e.off_code AND emp_id=?");

            ps.setString(1, empId);
            rs = ps.executeQuery();
            if (rs.next()) {
                String Loanee_name = rs.getString("fullname");
                loanvalue.setEmpName(Loanee_name);

                String Loanee_des = rs.getString("post");
                loanvalue.setDesignation(Loanee_des);

                String loanee_basicsalary = rs.getString("cur_basic_salary");
                String loanee_gp = rs.getString("gp");

                String basic_gp = "RS " + loanee_basicsalary + "/-" + " " + "+" + "" + "RS " + loanee_gp + "/-" + " (G.P)";
                loanvalue.setPay(basic_gp);

                loanvalue.setGpfno(rs.getString("gpf_no"));
                loanvalue.setOfficeAddress(rs.getString("department_name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }

        return loanvalue;
    }

    public void savegpfreapplyLoan(LoanGPFForm lform, String empid, String filepath) {
        Connection con = null;
        Calendar now = Calendar.getInstance();
        Date loanapplyDate = now.getTime();

        PreparedStatement ps = null;
        ResultSet rs = null;
        Date dt1 = null;
        Date dt2 = null;
        Date dt3 = null;

        DateFormat cdate = new SimpleDateFormat("dd-MMM-yyyy");
        MultipartFile loanAttch = lform.getFile_att();
        try {
            con = this.dataSource.getConnection();
            String forwardId = lform.getForwardtoHrmsid();
            String hidSpc = lform.getHidSPC();
            int loanvalue = 35;
            String initiatedSpc = lform.getEmpSPC();
            // String loanapplyfor=lform.getLoanapplyfor();
            String session_empId = empid;
            String gppftype = lform.getGpftype();
            String gppftype_value = gppftype.replace(",", "");

            String cbalnace = lform.getClosingbalance();
            String camount = lform.getCreditAmount();
            String ramount = lform.getRefund();
            String wamount = lform.getWithdrawalAmount();

            String nbalance = lform.getNetbalance();

            String amountrequired = lform.getWithdrawalreq();
            String purpose = lform.getPurpose();
            String rulegpf_request = lform.getRequestcovered();

            String samepurpose = lform.getWithdrawaltaken();
            String bcredit = lform.getBalanceCredit();
            int taskid = lform.getTaskid();
            int loanid = lform.getLoanId();
            String Comments = lform.getLoancomments();

            String strTaskmaster = "UPDATE   task_master SET status_id=?,pending_at=?,apply_to=?,pending_spc=?,apply_to_spc=?,initiated_on=?,note=? WHERE task_id=?";

            ps = con.prepareStatement(strTaskmaster);

            ps.setInt(1, loanvalue);
            ps.setString(2, forwardId);
            ps.setString(3, forwardId);

            ps.setString(4, hidSpc);
            ps.setString(5, hidSpc);
            ps.setTimestamp(6, new Timestamp(loanapplyDate.getTime()));
            ps.setString(7, "");
            ps.setInt(8, taskid);
            int stsTask = ps.executeUpdate();
            if (stsTask == 1) {

                String str = "UPDATE hw_emp_gpf_loan SET closing_balance=?,credit_amount=?,refund_amount=?,withdrawal_amount=?,net_balance=?,amount_required=?,purpose=?  ";
                str = str + ",rule_gpf=?,same_purpse_details=?,forward_to=?,loan_apply_date=?,loan_status=?,gpftype=?,balancecredit=?";
                str = str + ",comments=? WHERE loan_gpf_id=? ";
                ps = con.prepareStatement(str);
                if (cbalnace != null && !cbalnace.equals("")) {
                    ps.setDouble(1, Double.parseDouble(cbalnace));
                } else {
                    ps.setDouble(1, 0);
                }
                if (camount != null && !camount.equals("")) {
                    ps.setDouble(2, Double.parseDouble(camount));
                } else {
                    ps.setDouble(2, 0);
                }
                if (ramount != null && !ramount.equals("")) {
                    ps.setDouble(3, Double.parseDouble(ramount));
                } else {
                    ps.setDouble(3, 0);
                }
                if (wamount != null && !wamount.equals("")) {
                    ps.setDouble(4, Double.parseDouble(wamount));
                } else {
                    ps.setDouble(4, 0);
                }
                if (nbalance != null && !nbalance.equals("")) {
                    ps.setDouble(5, Double.parseDouble(nbalance));
                } else {
                    ps.setDouble(5, 0);
                }
                if (amountrequired != null && !amountrequired.equals("")) {
                    ps.setDouble(6, Double.parseDouble(amountrequired));
                } else {
                    ps.setDouble(6, 0);
                }
                ps.setString(7, purpose);
                ps.setString(8, rulegpf_request);
                ps.setString(9, samepurpose);
                ps.setString(10, hidSpc);
                ps.setTimestamp(11, new Timestamp(loanapplyDate.getTime()));
                ps.setInt(12, loanvalue);
                ps.setString(13, gppftype_value);
                if (bcredit != null && !bcredit.equals("")) {
                    ps.setDouble(14, Double.parseDouble(bcredit));
                } else {
                    ps.setDouble(14, 0);
                }
                ps.setString(15, Comments);
                ps.setInt(16, loanid);
                int sts = ps.executeUpdate();
                if (sts > 0) {
                    InputStream inputStream = null;
                    OutputStream outputStream = null;
                    String filename = "";
                    if (loanAttch != null && !loanAttch.isEmpty()) {
                        long time = System.currentTimeMillis();
                        filename = session_empId + "_" + taskid + "_" + time;

                        ps = con.prepareStatement("UPDATE hw_emp_gpf_loan SET original_filename=?,disk_file_name=?,file_type=? WHERE TASKID=?");
                        ps.setString(1, loanAttch.getOriginalFilename());
                        ps.setString(2, filename);
                        ps.setString(3, loanAttch.getContentType());
                        ps.setInt(4, taskid);
                        ps.executeUpdate();

                        String dirpath = filepath + "/";
                        File newfile = new File(dirpath);
                        if (!newfile.exists()) {
                            newfile.mkdirs();
                        }

                        outputStream = new FileOutputStream(dirpath + filename);
                        int read = 0;
                        byte[] bytes = new byte[1024];
                        inputStream = loanAttch.getInputStream();
                        while ((read = inputStream.read(bytes)) != -1) {
                            outputStream.write(bytes, 0, read);
                        }
                    }
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }

    }

    @Override
    public LoanHBAForm HBAEmpDetails(String hrmsid) {
        LoanHBAForm loanvalue = new LoanHBAForm();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Calendar now = Calendar.getInstance();
        int cmonth = now.get(Calendar.MONTH);
        int cyear = now.get(Calendar.YEAR);
        int previousYear = cyear;
        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("SELECT a.gpf_no,a.gp,a.emp_id,ARRAY_TO_STRING(ARRAY[INITIALS, a.F_NAME, a.M_NAME,"
                    + "a.L_NAME], ' ') fullname,a.cur_basic_salary,to_char(a.dob, 'DD-Mon-YYYY') as dob, to_char(a.dob+ cast('1 Year' as interval),'DD-Mon-YYYY')  as nextdob,a.cur_salary,to_char(a.JOINDATE_OF_GOO, 'DD-Mon-YYYY') as doj,"
                    + "to_char(a.dos, 'DD-Mon-YYYY') as dos,b.spn,b.spc,b.gpc,c.post,d.department_name,"
                    + "EXTRACT(YEAR from AGE(NOW(), a.JOINDATE_OF_GOO)) as age,EXTRACT(YEAR from AGE(a.dos,NOW())) as remainingage,e.ddo_code,e.off_en  FROM emp_mast a,g_spc b,g_post c,"
                    + "g_department d,g_office e WHERE  a.cur_spc=b.spc AND b.dept_code=d.department_code AND  "
                    + " b.gpc=c.post_code AND a.cur_off_code=e.off_code AND emp_id=?");
            ps.setString(1, hrmsid);
            rs = ps.executeQuery();
            if (rs.next()) {
                loanvalue.setEmpName(rs.getString("fullname"));
                String basicsalary_gp = rs.getString("cur_basic_salary") + "+ GP " + rs.getString("gp");
                loanvalue.setDesignation(rs.getString("post"));

                loanvalue.setDoj(rs.getString("doj"));
                loanvalue.setStationposted(rs.getString("off_en"));
                loanvalue.setDepartment(rs.getString("department_name"));
                loanvalue.setCursalary(rs.getString("cur_salary"));
                loanvalue.setCurbasicsalary(rs.getString("cur_basic_salary"));
                loanvalue.setEmpSPC(rs.getString("spc"));
                loanvalue.setNetPay(getNetPay(hrmsid, cmonth, cyear) + "");
                loanvalue.setDob(rs.getString("dob"));
                loanvalue.setSuperannuation(rs.getString("dos"));
                loanvalue.setNdob(rs.getString("nextdob"));

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return loanvalue;
    }

    @Override
    public void saveHBALoanData(LoanHBAForm lform, String empid, String filepath) {
        Connection con = null;
        Calendar now = Calendar.getInstance();
        Date loanapplyDate = now.getTime();

        PreparedStatement ps = null;
        ResultSet rs = null;
        Date dt1 = null;
        Date dt2 = null;
        Date dt3 = null;

        DateFormat cdate = new SimpleDateFormat("dd-MMM-yyyy");
        MultipartFile loanAttch = lform.getFile_att();
        MultipartFile loanAttch1 = lform.getFile_att1();
        try {
            con = this.dataSource.getConnection();
            String forwardId = lform.getForwardtoHrmsid();
            String hidSpc = lform.getHidSPC();
            int loanvalue = 49;
            String initiatedSpc = lform.getEmpSPC();
            String session_empId = empid;
            String jobtype = lform.getJobtype();
            String per_post = lform.getPer_post();
            String per_appointment = lform.getPer_appointment();
            String other_govt_ser = lform.getOther_govt_ser();
            String address = lform.getAddress();
            String floor_area = lform.getFloor_area();
            String approx_valuation = lform.getApprox_valuation();
            String reason = lform.getReason();
            String constructed_area = lform.getConstructed_area();
            String cost_land = lform.getCost_land();
            String cost_building = lform.getCost_building();
            String total_amount = lform.getTotal_amount();
            String amount_adv = lform.getAmount_adv();
            String noofYear = lform.getNoofYear();
            String city_name = lform.getCity_name();
            String settle_retir = lform.getSettle_retir();
            String area_plot = lform.getArea_plot();
            String localauthority = lform.getLocalauthority();
            String propose_acquire = lform.getPropose_acquire();
            String no_rooms = lform.getNo_rooms();
            String total_floor = lform.getTotal_floor();
            String additional_storey = lform.getAdditional_storey();
            String addition_room = lform.getAddition_room();
            String addition_farea = lform.getAddition_farea();
            String est_cost = lform.getEst_cost();
            String amount_desired = lform.getAmount_desired();
            String year_repaid = lform.getYear_repaid();
            String exact_location = lform.getExact_location();
            String exact_floor_area = lform.getExact_floor_area();
            String plinth_area = lform.getPlinth_area();
            String total_land_cost = lform.getTotal_land_cost();
            String parties_name_address = lform.getParties_name_address();
            String repay_adv_amount = lform.getRepay_adv_amount();
            String repay_year = lform.getRepay_year();
            String readymade_exact_loc = lform.getReadymade_exact_loc();
            String readymade_floor_area = lform.getReadymade_floor_area();
            String readymade_plinth_area = lform.getReadymade_plinth_area();
            String house_age = lform.getHouse_age();
            String valuation_price = lform.getValuation_price();
            String owner_name = lform.getOwner_name();
            String readymade_appro_amount = lform.getReadymade_appro_amount();
            String readymade_adv_amount = lform.getReadymade_adv_amount();
            String readymade_year = lform.getReadymade_year();
            String propose_amount = lform.getPropose_amount();
            String propose_adv = lform.getPropose_adv();
            String propose_repaid = lform.getPropose_repaid();
            String term_lease = lform.getTerm_lease();
            String term_expired = lform.getTerm_expired();
            String lease_condition = lform.getLease_condition();
            String premimum_paid = lform.getPremimum_paid();
            String annual_rent = lform.getAnnual_rent();
            String encumb_status = lform.getEncumb_status();
            String loanpurpose = lform.getLoanpurpose();

            String strTaskmaster = "INSERT INTO task_master (process_id "
                    + ",initiated_by "
                    + ",status_id "
                    + ",pending_at "
                    + ",apply_to "
                    + ",initiated_spc "
                    + ",pending_spc "
                    + ",apply_to_spc "
                    + ",initiated_on "
                    + ") values(?,?,?,?,?,?,?,?,?)";
            //  ps = con.prepareStatement(strTaskmaster);
            ps = con.prepareStatement(strTaskmaster, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, 9);
            ps.setString(2, session_empId);
            ps.setInt(3, loanvalue);
            ps.setString(4, forwardId);
            ps.setString(5, forwardId);
            ps.setString(6, initiatedSpc);
            ps.setString(7, hidSpc);
            ps.setString(8, hidSpc);
            ps.setTimestamp(9, new Timestamp(loanapplyDate.getTime()));

            int stsTask = ps.executeUpdate();
            rs = ps.getGeneratedKeys();
            rs.next();
            int taskId = rs.getInt("TASK_ID");

            if (stsTask == 1) {
                String str = "INSERT INTO hw_emp_hba_loan(taskid "
                        + ",emp_id "
                        + ",jobtype"
                        + ",per_post"
                        + ",per_appointment"
                        + ",other_govt_ser"
                        + ",address"
                        + ",floor_area"
                        + ",approx_valuation"
                        + ",reason"
                        + ",constructed_area"
                        + ",cost_land"
                        + ",cost_building"
                        + ",total_amount"
                        + ",amount_adv"
                        + ",noofyear"
                        + ",city_name"
                        + ",settle_retir"
                        + ",area_plot "
                        + ",localauthority"
                        + ",propose_acquire"
                        + ",no_rooms"
                        + ",total_floor"
                        + ",additional_storey"
                        + ",addition_room"
                        + ",addition_farea"
                        + ",est_cost"
                        + ",amount_desired"
                        + ",year_repaid"
                        + ",exact_location"
                        + ",exact_floor_area"
                        + ",plinth_area"
                        + ",total_land_cost"
                        + ",parties_name_address"
                        + ",repay_adv_amount"
                        + ",repay_year "
                        + ",readymade_exact_loc"
                        + ",readymade_floor_area"
                        + ",readymade_plinth_area"
                        + ",house_age"
                        + ",valuation_price"
                        + ",owner_name"
                        + ",readymade_appro_amount"
                        + ",readymade_adv_amount"
                        + ",readymade_year"
                        + ",propose_amount"
                        + ",propose_adv"
                        + ",propose_repaid"
                        + ",term_lease"
                        + ",term_expired"
                        + ",lease_condition"
                        + ",premimum_paid"
                        + ",annual_rent"
                        + ",encumb_status"
                        + ",forward_to"
                        + ",loan_status,loan_apply_date,purpose) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                ps = con.prepareStatement(str);
                ps.setInt(1, taskId);
                ps.setString(2, session_empId);
                ps.setString(3, jobtype);
                ps.setString(4, per_post);
                ps.setString(5, per_appointment);

                ps.setString(6, other_govt_ser);
                ps.setString(7, address);
                ps.setString(8, floor_area);
                ps.setString(9, approx_valuation);
                ps.setString(10, reason);
                ps.setString(11, constructed_area);
                ps.setString(12, cost_land);
                ps.setString(13, cost_building);
                ps.setString(14, total_amount);
                ps.setString(15, amount_adv);
                ps.setString(16, noofYear);
                ps.setString(17, city_name);
                ps.setString(18, settle_retir);
                ps.setString(19, area_plot);
                ps.setString(20, localauthority);
                ps.setString(21, propose_acquire);
                ps.setString(22, no_rooms);
                ps.setString(23, total_floor);
                ps.setString(24, additional_storey);
                ps.setString(25, addition_room);
                ps.setString(26, addition_farea);
                ps.setString(27, est_cost);
                ps.setString(28, amount_desired);
                ps.setString(29, year_repaid);
                ps.setString(30, exact_location);
                ps.setString(31, exact_floor_area);
                ps.setString(32, plinth_area);
                ps.setString(33, total_land_cost);
                ps.setString(34, parties_name_address);
                ps.setString(35, repay_adv_amount);
                ps.setString(36, repay_year);
                ps.setString(37, readymade_exact_loc);
                ps.setString(38, readymade_floor_area);
                ps.setString(39, readymade_plinth_area);
                ps.setString(40, house_age);
                ps.setString(41, valuation_price);
                ps.setString(42, owner_name);
                ps.setString(43, readymade_appro_amount);
                ps.setString(44, readymade_adv_amount);
                ps.setString(45, readymade_year);
                ps.setString(46, propose_amount);
                ps.setString(47, propose_adv);
                ps.setString(48, propose_repaid);
                ps.setString(49, term_lease);
                ps.setString(50, term_expired);
                ps.setString(51, lease_condition);
                ps.setString(52, premimum_paid);
                ps.setString(53, annual_rent);
                ps.setString(54, encumb_status);
                ps.setString(55, hidSpc);
                ps.setInt(56, loanvalue);
                ps.setTimestamp(57, new Timestamp(loanapplyDate.getTime()));
                ps.setString(58, loanpurpose);
                int sts = ps.executeUpdate();
                if (sts > 0) {
                    InputStream inputStream = null;
                    OutputStream outputStream = null;
                    String filename = "";
                    if (loanAttch != null && !loanAttch.isEmpty()) {
                        long time = System.currentTimeMillis();
                        filename = session_empId + "_1_" + taskId + "_" + time;

                        ps = con.prepareStatement("UPDATE hw_emp_hba_loan SET original_filename=?,disk_file_name=?,file_type=? WHERE TASKID=?");
                        ps.setString(1, loanAttch.getOriginalFilename());
                        ps.setString(2, filename);
                        ps.setString(3, loanAttch.getContentType());
                        ps.setInt(4, taskId);
                        ps.executeUpdate();

                        String dirpath = filepath + "/attachment/";
                        File newfile = new File(dirpath);
                        if (!newfile.exists()) {
                            newfile.mkdirs();
                        }

                        outputStream = new FileOutputStream(dirpath + filename);
                        int read = 0;
                        byte[] bytes = new byte[1024];
                        inputStream = loanAttch.getInputStream();
                        while ((read = inputStream.read(bytes)) != -1) {
                            outputStream.write(bytes, 0, read);
                        }
                    }

                    InputStream inputStream1 = null;
                    OutputStream outputStream1 = null;
                    String filename1 = "";
                    if (loanAttch1 != null && !loanAttch1.isEmpty()) {
                        long time = System.currentTimeMillis();
                        filename1 = session_empId + "_2_" + taskId + "_" + time;

                        ps = con.prepareStatement("UPDATE hw_emp_hba_loan SET original_filename1=?,disk_file_name1=?,file_type1=? WHERE TASKID=?");
                        ps.setString(1, loanAttch1.getOriginalFilename());
                        ps.setString(2, filename1);
                        ps.setString(3, loanAttch1.getContentType());
                        ps.setInt(4, taskId);
                        ps.executeUpdate();

                        String dirpath = filepath + "/attachment1/";
                        File newfile = new File(dirpath);
                        if (!newfile.exists()) {
                            newfile.mkdirs();
                        }

                        outputStream1 = new FileOutputStream(dirpath + filename1);
                        int read = 0;
                        byte[] bytes = new byte[1024];
                        inputStream1 = loanAttch1.getInputStream();
                        while ((read = inputStream1.read(bytes)) != -1) {
                            outputStream1.write(bytes, 0, read);
                        }
                    }

                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public LoanHBAForm gethbaLoanDetails(int taskid) {

        LoanHBAForm loanvalue = new LoanHBAForm();
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement pstask = null;
        PreparedStatement psapply = null;
        ResultSet rs = null;
        ResultSet rstask = null;
        ResultSet rsapply = null;
        Calendar now = Calendar.getInstance();
        int cmonth = now.get(Calendar.MONTH);
        int cyear = now.get(Calendar.YEAR);
        int previousYear = cyear;
        String hrmsid = null;
        int statusId = 0;
        int loanid = 0;

        try {
            con = this.dataSource.getConnection();
            pstask = con.prepareStatement("SELECT a.*,b.* FROM hw_emp_hba_loan a,task_master b WHERE a.taskid=b.task_id AND a.taskid='" + taskid + "' ");
            rstask = pstask.executeQuery();
            if (rstask.next()) {
                hrmsid = rstask.getString("emp_id");
                loanvalue.setApprovedBy(rstask.getString("apply_to"));
                loanvalue.setApprovedSpc(rstask.getString("pending_spc"));
                loanvalue.setLoanId(rstask.getInt("loan_hba_id"));
                loanvalue.setTaskid(rstask.getInt("taskid"));
                loanvalue.setLoancomments(rstask.getString("comments"));
                loanvalue.setJobtype(rstask.getString("jobtype"));
                loanvalue.setPer_post(rstask.getString("per_post"));
                loanvalue.setPer_appointment(rstask.getString("per_appointment"));
                loanvalue.setOther_govt_ser(rstask.getString("other_govt_ser"));
                loanvalue.setAddress(rstask.getString("address"));
                loanvalue.setFloor_area(rstask.getString("floor_area"));
                loanvalue.setApprox_valuation(rstask.getString("approx_valuation"));
                loanvalue.setReason(rstask.getString("reason"));
                loanvalue.setConstructed_area(rstask.getString("constructed_area"));
                loanvalue.setCost_land(rstask.getString("cost_land"));
                loanvalue.setCost_building(rstask.getString("cost_building"));
                loanvalue.setTotal_amount(rstask.getString("total_amount"));

                loanvalue.setAmount_adv(rstask.getString("amount_adv"));
                loanvalue.setNoofYear(rstask.getString("noofyear"));
                loanvalue.setCity_name(rstask.getString("city_name"));
                loanvalue.setSettle_retir(rstask.getString("settle_retir"));
                loanvalue.setArea_plot(rstask.getString("area_plot"));
                loanvalue.setLocalauthority(rstask.getString("localauthority"));
                loanvalue.setPropose_acquire(rstask.getString("propose_acquire"));
                loanvalue.setNo_rooms(rstask.getString("no_rooms"));
                loanvalue.setTotal_floor(rstask.getString("total_floor"));
                loanvalue.setAdditional_storey(rstask.getString("additional_storey"));

                loanvalue.setAddition_room(rstask.getString("addition_room"));
                loanvalue.setAddition_farea(rstask.getString("addition_farea"));
                loanvalue.setEst_cost(rstask.getString("est_cost"));
                loanvalue.setAmount_desired(rstask.getString("amount_desired"));
                loanvalue.setYear_repaid(rstask.getString("year_repaid"));
                loanvalue.setExact_location(rstask.getString("exact_location"));
                loanvalue.setExact_floor_area(rstask.getString("exact_floor_area"));
                loanvalue.setPlinth_area(rstask.getString("plinth_area"));
                loanvalue.setTotal_land_cost(rstask.getString("total_land_cost"));
                loanvalue.setParties_name_address(rstask.getString("parties_name_address"));

                loanvalue.setRepay_adv_amount(rstask.getString("repay_adv_amount"));
                loanvalue.setRepay_year(rstask.getString("repay_year"));
                loanvalue.setReadymade_exact_loc(rstask.getString("readymade_exact_loc"));
                loanvalue.setReadymade_floor_area(rstask.getString("readymade_floor_area"));
                loanvalue.setReadymade_plinth_area(rstask.getString("readymade_plinth_area"));
                loanvalue.setHouse_age(rstask.getString("house_age"));
                loanvalue.setValuation_price(rstask.getString("valuation_price"));
                loanvalue.setOwner_name(rstask.getString("owner_name"));
                loanvalue.setReadymade_appro_amount(rstask.getString("readymade_appro_amount"));
                loanvalue.setReadymade_adv_amount(rstask.getString("readymade_adv_amount"));

                loanvalue.setReadymade_year(rstask.getString("readymade_year"));
                loanvalue.setPropose_amount(rstask.getString("propose_amount"));
                loanvalue.setPropose_adv(rstask.getString("propose_adv"));
                loanvalue.setPropose_repaid(rstask.getString("propose_repaid"));
                loanvalue.setTerm_lease(rstask.getString("term_lease"));
                loanvalue.setTerm_expired(rstask.getString("term_expired"));
                loanvalue.setLease_condition(rstask.getString("lease_condition"));
                loanvalue.setPremimum_paid(rstask.getString("premimum_paid"));
                loanvalue.setAnnual_rent(rstask.getString("annual_rent"));
                loanvalue.setEncumb_status(rstask.getString("encumb_status"));
                loanvalue.setDiskFileName(rstask.getString("disk_file_name"));
                loanvalue.setDiskFileName1(rstask.getString("disk_file_name1"));
                loanvalue.setStatusId(rstask.getInt("status_id"));
                statusId = rstask.getInt("status_id");
                loanid = rstask.getInt("loan_hba_id");

            }
            ps = con.prepareStatement("SELECT a.gpf_no,a.gp,a.emp_id,ARRAY_TO_STRING(ARRAY[INITIALS, a.F_NAME, a.M_NAME,"
                    + "a.L_NAME], ' ') fullname,a.cur_basic_salary,to_char(a.dob, 'DD-Mon-YYYY') as dob, to_char(a.dob+ cast('1 Year' as interval),'DD-Mon-YYYY')  as nextdob,a.cur_salary,to_char(a.JOINDATE_OF_GOO, 'DD-Mon-YYYY') as doj,"
                    + "to_char(a.dos, 'DD-Mon-YYYY') as dos,b.spn,b.spc,b.gpc,c.post,d.department_name,"
                    + "EXTRACT(YEAR from AGE(NOW(), a.JOINDATE_OF_GOO)) as age,EXTRACT(YEAR from AGE(a.dos,NOW())) as remainingage,e.ddo_code,e.off_en  FROM emp_mast a,g_spc b,g_post c,"
                    + "g_department d,g_office e WHERE  a.cur_spc=b.spc AND b.dept_code=d.department_code AND  "
                    + " b.gpc=c.post_code AND a.cur_off_code=e.off_code AND emp_id=?");
            ps.setString(1, hrmsid);
            rs = ps.executeQuery();
            if (rs.next()) {
                loanvalue.setEmpName(rs.getString("fullname"));
                String basicsalary_gp = rs.getString("cur_basic_salary") + "+ GP " + rs.getString("gp");
                loanvalue.setDesignation(rs.getString("post"));
                loanvalue.setDoj(rs.getString("doj"));
                loanvalue.setStationposted(rs.getString("off_en"));
                loanvalue.setDepartment(rs.getString("department_name"));
                loanvalue.setCursalary(rs.getString("cur_salary"));
                loanvalue.setCurbasicsalary(rs.getString("cur_basic_salary"));
                loanvalue.setEmpSPC(rs.getString("spc"));
                loanvalue.setNetPay(getNetPay(hrmsid, cmonth, cyear) + "");
                loanvalue.setDob(rs.getString("dob"));
                loanvalue.setSuperannuation(rs.getString("dos"));
                loanvalue.setNdob(rs.getString("nextdob"));

            }
            if (statusId == 94 || statusId == 98) {
                pstask = con.prepareStatement("SELECT a.*,to_char(a.sanction_date, 'DD-Mon-YYYY') as fsanction_date,to_char(a.recovery_sdate, 'DD-Mon-YYYY') as frecovery_sdate FROM hw_loan_sanction_details a WHERE  a.taskid=? AND a.apply_loanid=? ");
                pstask.setInt(1, taskid);
                pstask.setInt(2, loanid);
                rstask = pstask.executeQuery();
                if (rstask.next()) {
                    loanvalue.setLetterNo(rstask.getString("sanction_no"));
                    loanvalue.setLetterDate(rstask.getString("fsanction_date"));
                    loanvalue.setSamount(rstask.getString("sanction_amount"));
                    loanvalue.setReleaseAmount(rstask.getString("release_amount"));
                    loanvalue.setEmiPrincipal(rstask.getInt("prinicipal_installment"));
                    loanvalue.setPrincipalAmount(rstask.getString("prinicipal_installment_amt"));
                    loanvalue.setTotalInterestamount(rstask.getString("interestamount"));
                    loanvalue.setEmiInterest(rstask.getInt("interest_installment"));
                    loanvalue.setInterestAmount(rstask.getString("interest_installment_amt"));
                    loanvalue.setInterestType(rstask.getString("interesttype"));
                    loanvalue.setRateInterest(rstask.getString("rate_interest"));
                    loanvalue.setLastInstallment(rstask.getString("last_installment_amt"));
                    loanvalue.setPenalRate(rstask.getString("penal_rate_interest"));
                    loanvalue.setMoratoriumRequired(rstask.getString("moratorium_required"));
                    loanvalue.setMoratoriumPeriod(rstask.getString("moratorium_period"));
                    loanvalue.setInsuranceFlag(rstask.getString("insurance_flag"));
                    loanvalue.setRecDate(rstask.getString("frecovery_sdate"));
                    loanvalue.setChatofAccount(rstask.getString("chat_account"));
                    loanvalue.setBillid(rstask.getString("bill_id"));
                    loanvalue.setDemandNo(rstask.getString("demand_no"));
                    loanvalue.setMajorhead(rstask.getString("major_head"));

                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return loanvalue;

    }

    @Override
    public void downloadHBALoanAttachment(HttpServletResponse response, String filepath, String loanid, String attchmentId) throws IOException {

        Connection con = null;

        Statement stmt = null;
        ResultSet rs = null;
        int BUFFER_LENGTH = 4096;
        OutputStream out = response.getOutputStream();
        try {
            con = dataSource.getConnection();

            stmt = con.createStatement();
            String sql = "SELECT * FROM hw_emp_hba_loan WHERE loan_hba_id='" + loanid + "'";
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                File f = null;
                String dirpath = null;
                
                if (attchmentId.equals("1")) {
                    dirpath = filepath + "/attachment/" + rs.getString("DISK_FILE_NAME");
                    
                } else {
                    dirpath = filepath + "/attachment1/" + rs.getString("DISK_FILE_NAME1");
                }

                f = new File(dirpath);
                if (f.exists()) {
                    String originalFilename = null;
                    String filetype = null;
                    if (attchmentId.equals("1")) {
                        originalFilename = rs.getString("ORIGINAL_FILENAME");
                        filetype = rs.getString("FILE_TYPE");
                    } else {
                        originalFilename = rs.getString("ORIGINAL_FILENAME1");
                        filetype = rs.getString("FILE_TYPE1");
                    }

                    response.setContentLength((int) f.length());
                    FileInputStream is = new FileInputStream(f);

                    response.setContentType(filetype);
                    response.setHeader("Content-Disposition", "attachment; filename=\"" + originalFilename + "\"");

                    byte[] bytes = new byte[BUFFER_LENGTH];
                    int read = 0;
                    while ((read = is.read(bytes, 0, BUFFER_LENGTH)) != -1) {
                        out.write(bytes, 0, read);
                    }
                    is.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, stmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    public void savehbaApproveLoanData(LoanHBAForm lform, String empid) {
        Connection con = null;
        Calendar now = Calendar.getInstance();
        Date loanapplyDate = now.getTime();

        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        Date dt1 = null;
        String pending_at = null;
        String pending_spc = null;
        int taskid = lform.getTaskid();
        int loanid = lform.getLoanId();
        int loanstatus = lform.getLoan_status();
        String Comments = lform.getLoancomments();
        String approvedby = lform.getApprovedBy();
        String approvedspc = lform.getApprovedSpc();
        String gpfSeries = "";
        String gpfnumber = "";
        DateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");

        MultipartFile loanAttch = lform.getFile_att();
        try {
            con = this.dataSource.getConnection();
            if (loanstatus == 51 || loanstatus == 53 || loanstatus == 94 || loanstatus == 98) {
                String forwardId = lform.getForwardtoHrmsid();
                String hidSpc = lform.getHidSPC();
                ps = con.prepareStatement(" SELECT * FROM task_master WHERE  task_id='" + taskid + "'");
                rs = ps.executeQuery();
                while (rs.next()) {
                    pending_at = (rs.getString("pending_at"));
                    pending_spc = (rs.getString("pending_spc"));
                }

                ps = con.prepareStatement("UPDATE task_master SET status_id=?,pending_at=?,note=?,apply_to=?,pending_spc=?,apply_to_spc=? WHERE task_id=?");
                ps.setInt(1, loanstatus);
                ps.setString(2, forwardId);
                ps.setString(3, Comments);
                ps.setString(4, forwardId);
                ps.setString(5, hidSpc);
                ps.setString(6, hidSpc);
                ps.setInt(7, taskid);
                int stsTask = ps.executeUpdate();
                if (stsTask == 1) {
                    String str = "INSERT INTO workflow_log(task_id "
                            + ",task_action_date "
                            + ",action_taken_by"
                            + ",spc_ontime"
                            + ",task_status_id"
                            + ",note"
                            + ",forward_to"
                            + ",forwarded_spc"                         
                            + ",ref_id"
                            + ") values(?,?,?,?,?,?,?,?,?)";
                    ps = con.prepareStatement(str);
                    ps.setInt(1, taskid);
                    ps.setTimestamp(2, new Timestamp(loanapplyDate.getTime()));
                    ps.setString(3, pending_at);
                    ps.setString(4, pending_spc);
                    ps.setInt(5, loanstatus);
                    ps.setString(6, Comments);
                    ps.setString(7, forwardId);
                    ps.setString(8, hidSpc);
                    ps.setInt(9, loanid);
                //    int wfcode = CommonFunctions.getMaxCodeInteger("workflow_log", "log_id", con);
                 //   ps.setInt(9, wfcode);
                    ps.executeUpdate();

                    ps = con.prepareStatement("UPDATE hw_emp_hba_loan SET loan_status=?,comments=? WHERE taskid=? AND loan_hba_id=?");
                    ps.setInt(1, loanstatus);
                    ps.setString(2, Comments);
                    ps.setInt(3, taskid);
                    ps.setInt(4, loanid);
                    ps.executeUpdate();

                    if (loanstatus == 51) {
                        ps = con.prepareStatement("SELECT * FROM  hw_emp_hba_loan WHERE taskid=? AND loan_hba_id=? LIMIT 1");
                        ps.setInt(1, taskid);
                        ps.setInt(2, loanid);
                        rs = ps.executeQuery();
                        if (rs.next()) {
                            Date approverdate = now.getTime();
                            String loanApplyId = rs.getString("emp_id");
                            String loanshortcode = "HBA";
                            String chatofaccount = "057610002010825480160001110";
                            String majorhead = "7610";
                            String billid = "09";
                            Users ue = getLoanEmpDetail(loanApplyId);
                            String ifmsLoanId = loanshortcode + ue.getGpfno();
                            String str1 = "INSERT INTO hw_loan_sanction_details(taskid "
                                    + ",apply_loanId "
                                    + ",loan_type"
                                    + ",loanee_id"
                                    + ",approver_id"
                                    + ",approved_date"
                                    + ",gpfNo"
                                    + ",accountNo"
                                    + ",pranNo"
                                    + ",name"
                                    + ",designation"
                                    + ",office_name"
                                    + ",dob"
                                    + ",dos"
                                    + ",basic_pay"
                                    + ",gp"
                                    + ",empType"
                                    + ",address"
                                    + ",gpf_series"
                                    + ",gpf_accountno"
                                    + ",ifms_loanid"
                                    + ",demand_no"
                                    + ",major_head"
                                    + ",chat_account"
                                    + ",bill_id"
                                    + ",ddo_code"
                                    + ",bank_account"
                                    + ",ifsc_code"
                                    + ",mobile"
                                    + ") values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                            ps1 = con.prepareStatement(str1);
                            ps1.setInt(1, taskid);
                            ps1.setInt(2, loanid);
                            ps1.setString(3, "HBA");
                            ps1.setString(4, rs.getString("emp_id"));
                            ps1.setString(5, empid);
                            ps1.setTimestamp(6, new Timestamp(approverdate.getTime()));
                            if (ue.getAcctType().equals("GPF")) {
                                gpfnumber = getempNumberFromGPF(ue.getGpfno());
                                gpfSeries = CommonFunctions.getGPFSeries(ue.getGpfno());
                                ps1.setString(7, ue.getGpfno());
                                ps1.setString(8, ue.getAcctType());
                                ps1.setString(9, "");

                            } else {
                                ps1.setString(7, "");
                                ps1.setString(8, ue.getAcctType());
                                ps1.setString(9, ue.getGpfno());
                                gpfnumber = ue.getGpfno();

                            }

                            ps1.setString(10, ue.getFullName());
                            ps1.setString(11, ue.getPostname());
                            ps1.setString(12, ue.getOffname());
                            ps1.setTimestamp(13, new Timestamp(sdf.parse(ue.getEmpDob()).getTime()));
                            ps1.setTimestamp(14, new Timestamp(sdf.parse(ue.getEmpDos()).getTime()));
                            ps1.setDouble(15, Double.parseDouble(ue.getBasicsalry()));
                            ps1.setDouble(16, Double.parseDouble(ue.getGp()));
                            // ps1.setString(15, rs.getString("cur_basic_salary"));
                            // ps1.setString(16, rs.getString("gp"));
                            if (ue.getIsRegular().equals("Y")) {
                                ps1.setString(17, "Permanent");
                                //	loanvalue.setJobType("Permanent");
                            } else {
                                ps1.setString(17, "Temporary");
                                //loanvalue.setJobType("Temporary");
                            }
                            ps1.setString(18, ue.getAddress());
                            ps1.setString(19, gpfSeries);
                            ps1.setString(20, gpfnumber);
                            ps1.setString(21, ifmsLoanId);
                            ps1.setString(22, "05");
                            ps1.setString(23, majorhead);
                            ps1.setString(24, chatofaccount);
                            ps1.setString(25, billid);
                            ps1.setString(26, ue.getDdoCode());
                            ps1.setString(27, ue.getBankAccNo());
                            ps1.setString(28, ue.getIfmsCode());
                            ps1.setString(29, ue.getMobile());
                            ps1.executeUpdate();
                        }

                    }
                    if (loanstatus == 94) {
                        Date entryDate = now.getTime();
                        ps1 = con.prepareStatement("UPDATE hw_loan_sanction_details SET issue_letter_login_id=?,issue_letter_entry_date=?,sanction_no=?,sanction_date=? WHERE taskid=? AND apply_loanid=?");

                        ps1.setString(1, empid);
                        ps1.setTimestamp(2, new Timestamp(entryDate.getTime()));

                        ps1.setString(3, lform.getLetterNo());
                        if (lform.getLetterDate() != null && !lform.getLetterDate().equals("")) {
                            ps1.setTimestamp(4, new Timestamp(sdf.parse(lform.getLetterDate()).getTime()));
                        } else {
                            ps1.setTimestamp(4, null);
                        }
                        ps1.setInt(5, taskid);
                        ps1.setInt(6, loanid);
                        ps1.executeUpdate();

                    }
                    if (loanstatus == 98) {
                        Date entryDate = now.getTime();
                        ps1 = con.prepareStatement("UPDATE hw_loan_sanction_details SET loan_sanction_login_id=?,sanction_entry_date=?,interesttype=?,sanction_amount=?,release_amount=?,prinicipal_installment=?,prinicipal_installment_amt=?,interest_installment=?,interest_installment_amt=?,rate_interest=?,last_installment_amt=?,penal_rate_interest=?,moratorium_required=?,moratorium_period=?,insurance_flag=?,recovery_sdate=?,interestamount=? WHERE taskid=? AND apply_loanid=?");

                        ps1.setString(1, empid);
                        ps1.setTimestamp(2, new Timestamp(entryDate.getTime()));
                        ps1.setString(3, lform.getInterestType());
                        String sanction_amount = lform.getSamount();
                        String release_amount = lform.getRamount();
                        if (sanction_amount != null && !sanction_amount.equals("")) {
                            ps1.setDouble(4, Double.parseDouble(sanction_amount));
                        } else {
                            ps1.setDouble(4, 0);
                        }
                        if (release_amount != null && !release_amount.equals("")) {
                            ps1.setDouble(5, Double.parseDouble(release_amount));
                        } else {
                            ps1.setDouble(5, 0);
                        }
                        ps1.setInt(6, lform.getEmiPrincipal());
                        String PrincipalAmount = lform.getPrincipalAmount();
                        ps1.setDouble(7, Double.parseDouble(PrincipalAmount));
                        ps1.setInt(8, lform.getEmiInterest());
                        ps1.setDouble(9, Double.parseDouble(lform.getInterestAmount()));
                        ps1.setDouble(10, Double.parseDouble(lform.getRateInterest()));
                        String last_installment_amt = lform.getLastInstallment();
                        if (last_installment_amt != null && !last_installment_amt.equals("")) {
                            ps1.setDouble(11, Double.parseDouble(last_installment_amt));
                        } else {
                            ps1.setDouble(11, 0);
                        }
                        String penal_rate_interest = lform.getPenalRate();
                        if (penal_rate_interest != null && !penal_rate_interest.equals("")) {
                            ps1.setDouble(12, Double.parseDouble(penal_rate_interest));
                        } else {
                            ps1.setDouble(12, 0);
                        }
                        ps1.setString(13, lform.getMoratoriumRequired());
                        ps1.setString(14, lform.getMoratoriumPeriod());
                        ps1.setString(15, lform.getInsuranceFlag());
                        // String recDate = lform.getRecDate();
                        if (lform.getRecDate() != null && !lform.getRecDate().equals("")) {
                            ps1.setTimestamp(16, new Timestamp(sdf.parse(lform.getRecDate()).getTime()));
                        } else {
                            ps1.setTimestamp(16, null);
                        }

                        ps1.setDouble(17, Double.parseDouble(lform.getTotalInterestamount()));

                        ps1.setInt(18, taskid);
                        ps1.setInt(19, loanid);
                        ps1.executeUpdate();

                    }

                }

            } else {
                ps = con.prepareStatement("UPDATE hw_emp_hba_loan SET loan_status=?,approved_by=?,approved_spc=?,comments=?,loan_sanction_date=? WHERE taskid=? AND loan_hba_id=?");
                ps.setInt(1, loanstatus);
                ps.setString(2, approvedby);
                ps.setString(3, approvedspc);
                ps.setString(4, Comments);
                ps.setTimestamp(5, new Timestamp(loanapplyDate.getTime()));
                ps.setInt(6, taskid);
                ps.setInt(7, loanid);
                int stsTask = ps.executeUpdate();

                if (stsTask == 1) {
                    ps = con.prepareStatement("UPDATE task_master SET status_id=?,pending_at=?,note=?,apply_to=?,pending_spc=?,apply_to_spc=? WHERE task_id=?");
                    ps.setInt(1, loanstatus);
                    ps.setString(2, null);
                    ps.setString(3, Comments);
                    ps.setString(4, null);
                    ps.setString(5, null);
                    ps.setString(6, null);
                    ps.setInt(7, taskid);
                    ps.executeUpdate();

                    if (loanstatus == 50) {
                        Date entryDate = now.getTime();
                        ps1 = con.prepareStatement("UPDATE hw_loan_sanction_details SET bill_create_id=?,bill_entry_date=?,bill_no=?,bill_date=?,bill_amount=?,bill_description=? WHERE taskid=? AND apply_loanid=?");

                        ps1.setString(1, empid);
                        ps1.setTimestamp(2, new Timestamp(entryDate.getTime()));
                        ps1.setString(3, lform.getBillno());
                        if (lform.getBilldate() != null && !lform.getBilldate().equals("")) {
                            ps1.setTimestamp(4, new Timestamp(sdf.parse(lform.getBilldate()).getTime()));
                        } else {
                            ps1.setTimestamp(4, null);
                        }

                        String bill_amount = lform.getBillAmount();
                        if (bill_amount != null && !bill_amount.equals("")) {
                            ps1.setDouble(5, Double.parseDouble(bill_amount));
                        } else {
                            ps1.setDouble(5, 0);
                        }
                        ps1.setString(6, lform.getBillDescription());
                        ps1.setInt(7, taskid);
                        ps1.setInt(8, loanid);
                        ps1.executeUpdate();

                    }
                }    
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    public LoanHBAForm ReplyhbaLoan(String option, String hrmsid, int loanid) {

        LoanHBAForm loanvalue = new LoanHBAForm();
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement pstask = null;
        PreparedStatement psapply = null;
        ResultSet rs = null;
        ResultSet rstask = null;
        ResultSet rsapply = null;
        Calendar now = Calendar.getInstance();
        int cmonth = now.get(Calendar.MONTH);
        int cyear = now.get(Calendar.YEAR);
        int previousYear = cyear;

        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("SELECT a.gpf_no,a.gp,a.emp_id,ARRAY_TO_STRING(ARRAY[INITIALS, a.F_NAME, a.M_NAME,"
                    + "a.L_NAME], ' ') fullname,a.cur_basic_salary,to_char(a.dob, 'DD-Mon-YYYY') as dob, to_char(a.dob+ cast('1 Year' as interval),'DD-Mon-YYYY')  as nextdob,a.cur_salary,to_char(a.JOINDATE_OF_GOO, 'DD-Mon-YYYY') as doj,"
                    + "to_char(a.dos, 'DD-Mon-YYYY') as dos,b.spn,b.spc,b.gpc,c.post,d.department_name,"
                    + "EXTRACT(YEAR from AGE(NOW(), a.JOINDATE_OF_GOO)) as age,EXTRACT(YEAR from AGE(a.dos,NOW())) as remainingage,e.ddo_code,e.off_en  FROM emp_mast a,g_spc b,g_post c,"
                    + "g_department d,g_office e WHERE  a.cur_spc=b.spc AND b.dept_code=d.department_code AND  "
                    + " b.gpc=c.post_code AND a.cur_off_code=e.off_code AND emp_id=?");
            ps.setString(1, hrmsid);
            rs = ps.executeQuery();
            if (rs.next()) {
                loanvalue.setEmpName(rs.getString("fullname"));
                String basicsalary_gp = rs.getString("cur_basic_salary") + "+ GP " + rs.getString("gp");
                loanvalue.setDesignation(rs.getString("post"));

                loanvalue.setDoj(rs.getString("doj"));
                loanvalue.setStationposted(rs.getString("off_en"));
                loanvalue.setDepartment(rs.getString("department_name"));
                loanvalue.setCursalary(rs.getString("cur_salary"));
                loanvalue.setCurbasicsalary(rs.getString("cur_basic_salary"));
                loanvalue.setEmpSPC(rs.getString("spc"));
                loanvalue.setNetPay(getNetPay(hrmsid, cmonth, cyear) + "");
                loanvalue.setDob(rs.getString("dob"));
                loanvalue.setSuperannuation(rs.getString("dos"));
                loanvalue.setNdob(rs.getString("nextdob"));

            }
            pstask = con.prepareStatement("SELECT a.*,b.* FROM hw_emp_hba_loan a,task_master b WHERE a.taskid=b.task_id AND a.loan_hba_id='" + loanid + "' ");
            rstask = pstask.executeQuery();
            if (rstask.next()) {
                loanvalue.setApprovedBy(rstask.getString("apply_to"));
                loanvalue.setApprovedSpc(rstask.getString("pending_spc"));
                loanvalue.setLoanId(rstask.getInt("loan_hba_id"));
                loanvalue.setTaskid(rstask.getInt("taskid"));
                loanvalue.setLoancomments(rstask.getString("comments"));
                loanvalue.setJobtype(rstask.getString("jobtype"));
                loanvalue.setPer_post(rstask.getString("per_post"));
                loanvalue.setPer_appointment(rstask.getString("per_appointment"));
                loanvalue.setOther_govt_ser(rstask.getString("other_govt_ser"));
                loanvalue.setAddress(rstask.getString("address"));
                loanvalue.setFloor_area(rstask.getString("floor_area"));
                loanvalue.setApprox_valuation(rstask.getString("approx_valuation"));
                loanvalue.setReason(rstask.getString("reason"));
                loanvalue.setConstructed_area(rstask.getString("constructed_area"));
                loanvalue.setCost_land(rstask.getString("cost_land"));
                loanvalue.setCost_building(rstask.getString("cost_building"));
                loanvalue.setTotal_amount(rstask.getString("total_amount"));

                loanvalue.setAmount_adv(rstask.getString("amount_adv"));
                loanvalue.setNoofYear(rstask.getString("noofyear"));
                loanvalue.setCity_name(rstask.getString("city_name"));
                loanvalue.setSettle_retir(rstask.getString("settle_retir"));
                loanvalue.setArea_plot(rstask.getString("area_plot"));
                loanvalue.setLocalauthority(rstask.getString("localauthority"));
                loanvalue.setPropose_acquire(rstask.getString("propose_acquire"));
                loanvalue.setNo_rooms(rstask.getString("no_rooms"));
                loanvalue.setTotal_floor(rstask.getString("total_floor"));
                loanvalue.setAdditional_storey(rstask.getString("additional_storey"));

                loanvalue.setAddition_room(rstask.getString("addition_room"));
                loanvalue.setAddition_farea(rstask.getString("addition_farea"));
                loanvalue.setEst_cost(rstask.getString("est_cost"));
                loanvalue.setAmount_desired(rstask.getString("amount_desired"));
                loanvalue.setYear_repaid(rstask.getString("year_repaid"));
                loanvalue.setExact_location(rstask.getString("exact_location"));
                loanvalue.setExact_floor_area(rstask.getString("exact_floor_area"));
                loanvalue.setPlinth_area(rstask.getString("plinth_area"));
                loanvalue.setTotal_land_cost(rstask.getString("total_land_cost"));
                loanvalue.setParties_name_address(rstask.getString("parties_name_address"));

                loanvalue.setRepay_adv_amount(rstask.getString("repay_adv_amount"));
                loanvalue.setRepay_year(rstask.getString("repay_year"));
                loanvalue.setReadymade_exact_loc(rstask.getString("readymade_exact_loc"));
                loanvalue.setReadymade_floor_area(rstask.getString("readymade_floor_area"));
                loanvalue.setReadymade_plinth_area(rstask.getString("readymade_plinth_area"));
                loanvalue.setHouse_age(rstask.getString("house_age"));
                loanvalue.setValuation_price(rstask.getString("valuation_price"));
                loanvalue.setOwner_name(rstask.getString("owner_name"));
                loanvalue.setReadymade_appro_amount(rstask.getString("readymade_appro_amount"));
                loanvalue.setReadymade_adv_amount(rstask.getString("readymade_adv_amount"));

                loanvalue.setReadymade_year(rstask.getString("readymade_year"));
                loanvalue.setPropose_amount(rstask.getString("propose_amount"));
                loanvalue.setPropose_adv(rstask.getString("propose_adv"));
                loanvalue.setPropose_repaid(rstask.getString("propose_repaid"));
                loanvalue.setTerm_lease(rstask.getString("term_lease"));
                loanvalue.setTerm_expired(rstask.getString("term_expired"));
                loanvalue.setLease_condition(rstask.getString("lease_condition"));
                loanvalue.setPremimum_paid(rstask.getString("premimum_paid"));
                loanvalue.setAnnual_rent(rstask.getString("annual_rent"));
                loanvalue.setEncumb_status(rstask.getString("encumb_status"));
                loanvalue.setDiskFileName(rstask.getString("disk_file_name"));
                loanvalue.setDiskFileName1(rstask.getString("disk_file_name1"));

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return loanvalue;

    }

    public void saveHBAreapplyLoan(LoanHBAForm lform, String empid, String filepath) {
        Connection con = null;
        Calendar now = Calendar.getInstance();
        Date loanapplyDate = now.getTime();

        PreparedStatement ps = null;
        ResultSet rs = null;
        Date dt1 = null;
        Date dt2 = null;
        Date dt3 = null;

        DateFormat cdate = new SimpleDateFormat("dd-MMM-yyyy");
        MultipartFile loanAttch = lform.getFile_att();
        MultipartFile loanAttch1 = lform.getFile_att1();
        try {
            con = this.dataSource.getConnection();
            String forwardId = lform.getForwardtoHrmsid();
            String hidSpc = lform.getHidSPC();
            int loanvalue = 43;
            String initiatedSpc = lform.getEmpSPC();
            // String loanapplyfor=lform.getLoanapplyfor();
            String session_empId = empid;
            int taskid = lform.getTaskid();
            int loanid = lform.getLoanId();
            String Comments = lform.getLoancomments();
            String jobtype = lform.getJobtype();
            String per_post = lform.getPer_post();
            String per_appointment = lform.getPer_appointment();
            String other_govt_ser = lform.getOther_govt_ser();
            String address = lform.getAddress();
            String floor_area = lform.getFloor_area();
            String approx_valuation = lform.getApprox_valuation();
            String reason = lform.getReason();
            String constructed_area = lform.getConstructed_area();
            String cost_land = lform.getCost_land();
            String cost_building = lform.getCost_building();
            String total_amount = lform.getTotal_amount();
            String amount_adv = lform.getAmount_adv();
            String noofYear = lform.getNoofYear();
            String city_name = lform.getCity_name();
            String settle_retir = lform.getSettle_retir();
            String area_plot = lform.getArea_plot();
            String localauthority = lform.getLocalauthority();
            String propose_acquire = lform.getPropose_acquire();
            String no_rooms = lform.getNo_rooms();
            String total_floor = lform.getTotal_floor();
            String additional_storey = lform.getAdditional_storey();
            String addition_room = lform.getAddition_room();
            String addition_farea = lform.getAddition_farea();
            String est_cost = lform.getEst_cost();
            String amount_desired = lform.getAmount_desired();
            String year_repaid = lform.getYear_repaid();
            String exact_location = lform.getExact_location();
            String exact_floor_area = lform.getExact_floor_area();
            String plinth_area = lform.getPlinth_area();
            String total_land_cost = lform.getTotal_land_cost();
            String parties_name_address = lform.getParties_name_address();
            String repay_adv_amount = lform.getRepay_adv_amount();
            String repay_year = lform.getRepay_year();
            String readymade_exact_loc = lform.getReadymade_exact_loc();
            String readymade_floor_area = lform.getReadymade_floor_area();
            String readymade_plinth_area = lform.getReadymade_plinth_area();
            String house_age = lform.getHouse_age();
            String valuation_price = lform.getValuation_price();
            String owner_name = lform.getOwner_name();
            String readymade_appro_amount = lform.getReadymade_appro_amount();
            String readymade_adv_amount = lform.getReadymade_adv_amount();
            String readymade_year = lform.getReadymade_year();
            String propose_amount = lform.getPropose_amount();
            String propose_adv = lform.getPropose_adv();
            String propose_repaid = lform.getPropose_repaid();
            String term_lease = lform.getTerm_lease();
            String term_expired = lform.getTerm_expired();
            String lease_condition = lform.getLease_condition();
            String premimum_paid = lform.getPremimum_paid();
            String annual_rent = lform.getAnnual_rent();
            String encumb_status = lform.getEncumb_status();

            String strTaskmaster = "UPDATE   task_master SET status_id=?,pending_at=?,apply_to=?,pending_spc=?,apply_to_spc=?,initiated_on=?,note=? WHERE task_id=?";

            ps = con.prepareStatement(strTaskmaster);

            ps.setInt(1, loanvalue);
            ps.setString(2, forwardId);
            ps.setString(3, forwardId);

            ps.setString(4, hidSpc);
            ps.setString(5, hidSpc);
            ps.setTimestamp(6, new Timestamp(loanapplyDate.getTime()));
            ps.setString(7, "");
            ps.setInt(8, taskid);
            int stsTask = ps.executeUpdate();
            if (stsTask == 1) {
                String str = "UPDATE hw_emp_hba_loan SET taskid =?,emp_id=?,jobtype=?,per_post=?,per_appointment=?,other_govt_ser=?,address=?,floor_area=?,approx_valuation=?  ";
                str = str + ",reason=?,constructed_area=?,cost_land=?,cost_building=?,total_amount=?,amount_adv=?,noofyear=?";
                str = str + ",city_name=?,settle_retir=?,area_plot=?,localauthority=?,propose_acquire=?,no_rooms=?,total_floor=?";
                str = str + ",additional_storey=?,addition_room=?,addition_farea=?,est_cost=?,amount_desired=?,year_repaid=?,exact_location=?";
                str = str + ",exact_floor_area=?,plinth_area=?,total_land_cost=?,parties_name_address=?,repay_adv_amount=?,repay_year=?,readymade_exact_loc=?";
                str = str + ",readymade_floor_area=?,readymade_plinth_area=?,house_age=?,valuation_price=?,owner_name=?,readymade_appro_amount=?,readymade_adv_amount=?";
                str = str + ",readymade_year=?,propose_amount=?,propose_adv=?,propose_repaid=?,term_lease=?,term_expired=?,lease_condition=?";
                str = str + ",premimum_paid=?,annual_rent=?,encumb_status=?,forward_to=?,loan_status=?";
                str = str + ",comments=? WHERE loan_hba_id =? ";
                ps = con.prepareStatement(str);
                ps.setInt(1, taskid);
                ps.setString(2, session_empId);
                ps.setString(3, jobtype);
                ps.setString(4, per_post);
                ps.setString(5, per_appointment);

                ps.setString(6, other_govt_ser);
                ps.setString(7, address);
                ps.setString(8, floor_area);
                ps.setString(9, approx_valuation);
                ps.setString(10, reason);
                ps.setString(11, constructed_area);
                ps.setString(12, cost_land);
                ps.setString(13, cost_building);
                ps.setString(14, total_amount);
                ps.setString(15, amount_adv);
                ps.setString(16, noofYear);
                ps.setString(17, city_name);
                ps.setString(18, settle_retir);
                ps.setString(19, area_plot);
                ps.setString(20, localauthority);
                ps.setString(21, propose_acquire);
                ps.setString(22, no_rooms);
                ps.setString(23, total_floor);
                ps.setString(24, additional_storey);
                ps.setString(25, addition_room);
                ps.setString(26, addition_farea);
                ps.setString(27, est_cost);
                ps.setString(28, amount_desired);
                ps.setString(29, year_repaid);
                ps.setString(30, exact_location);
                ps.setString(31, exact_floor_area);
                ps.setString(32, plinth_area);
                ps.setString(33, total_land_cost);
                ps.setString(34, parties_name_address);
                ps.setString(35, repay_adv_amount);
                ps.setString(36, repay_year);
                ps.setString(37, readymade_exact_loc);
                ps.setString(38, readymade_floor_area);
                ps.setString(39, readymade_plinth_area);
                ps.setString(40, house_age);
                ps.setString(41, valuation_price);
                ps.setString(42, owner_name);
                ps.setString(43, readymade_appro_amount);
                ps.setString(44, readymade_adv_amount);
                ps.setString(45, readymade_year);
                ps.setString(46, propose_amount);
                ps.setString(47, propose_adv);
                ps.setString(48, propose_repaid);
                ps.setString(49, term_lease);
                ps.setString(50, term_expired);
                ps.setString(51, lease_condition);
                ps.setString(52, premimum_paid);
                ps.setString(53, annual_rent);
                ps.setString(54, encumb_status);
                ps.setString(55, hidSpc);
                ps.setInt(56, loanvalue);

                ps.setString(57, Comments);
                ps.setInt(58, loanid);
                int sts = ps.executeUpdate();
                if (sts > 0) {
                    InputStream inputStream = null;
                    OutputStream outputStream = null;
                    String filename = "";
                    if (loanAttch != null && !loanAttch.isEmpty()) {
                        long time = System.currentTimeMillis();
                        filename = session_empId + "_1_" + taskid + "_" + time;

                        ps = con.prepareStatement("UPDATE hw_emp_hba_loan SET original_filename=?,disk_file_name=?,file_type=? WHERE TASKID=?");
                        ps.setString(1, loanAttch.getOriginalFilename());
                        ps.setString(2, filename);
                        ps.setString(3, loanAttch.getContentType());
                        ps.setInt(4, taskid);
                        ps.executeUpdate();

                        String dirpath = filepath + "/attachment/";
                        File newfile = new File(dirpath);
                        if (!newfile.exists()) {
                            newfile.mkdirs();
                        }

                        outputStream = new FileOutputStream(dirpath + filename);
                        int read = 0;
                        byte[] bytes = new byte[1024];
                        inputStream = loanAttch.getInputStream();
                        while ((read = inputStream.read(bytes)) != -1) {
                            outputStream.write(bytes, 0, read);
                        }
                    }

                    InputStream inputStream1 = null;
                    OutputStream outputStream1 = null;
                    String filename1 = "";
                    if (loanAttch1 != null && !loanAttch1.isEmpty()) {
                        long time = System.currentTimeMillis();
                        filename1 = session_empId + "_2_" + taskid + "_" + time;

                        ps = con.prepareStatement("UPDATE hw_emp_hba_loan SET original_filename1=?,disk_file_name1=?,file_type1=? WHERE TASKID=?");
                        ps.setString(1, loanAttch1.getOriginalFilename());
                        ps.setString(2, filename1);
                        ps.setString(3, loanAttch1.getContentType());
                        ps.setInt(4, taskid);
                        ps.executeUpdate();

                        String dirpath = filepath + "/attachment1/";
                        File newfile = new File(dirpath);
                        if (!newfile.exists()) {
                            newfile.mkdirs();
                        }

                        outputStream1 = new FileOutputStream(dirpath + filename1);
                        int read = 0;
                        byte[] bytes = new byte[1024];
                        inputStream1 = loanAttch.getInputStream();
                        while ((read = inputStream.read(bytes)) != -1) {
                            outputStream1.write(bytes, 0, read);
                        }
                    }

                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }

    }

    @Override
    public void viewPDFfunc(Document document, int taskId, String hrmsid) {
        LoanForm loanvalue = new LoanForm();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        PreparedStatement ps1 = null;

        Calendar now = Calendar.getInstance();

        int cyear = now.get(Calendar.YEAR);
        int nextYear = cyear + 1;
        String fyear = cyear + " - " + nextYear;

        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("select * from hw_loan_sanction_details HD INNER JOIN hw_emp_loan HL ON HD.taskid = HL.taskid WHERE HL.taskid = ?");
            ps.setInt(1, taskId);

            rs = ps.executeQuery();
            rs.next();
            Font f1 = new Font();
            f1.setSize(10);
            f1.setFamily("Times New Roman");
            Font hdrTextFont = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD, BaseColor.BLACK);
            Font hdrTextFont1 = new Font(Font.FontFamily.HELVETICA, 6, Font.BOLD, BaseColor.BLACK);
            Font dataHdrFont = new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD, BaseColor.BLACK);
            Font dataValFont = new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK);
            final Font pFont = FontFactory.getFont("Arial", 10, Font.NORMAL);
            final Font bFont = FontFactory.getFont("Arial", 10, Font.BOLD);
            final Font bottomRule = FontFactory.getFont("Arial", 11, Font.NORMAL);

            PdfPTable table = null;

            table = new PdfPTable(1);
            table.setWidths(new int[]{5});
            table.setWidthPercentage(100);

            PdfPCell cell = null;

            cell = new PdfPCell(new Phrase("GOVERNMENT OF ODISHA", hdrTextFont));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(rs.getString("office_name") + " DEPARTMENT", hdrTextFont));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            document.add(table);

            PdfPTable table1 = new PdfPTable(2);
            table1.setWidths(new int[]{5, 5});
            table1.setWidthPercentage(100);
            PdfPCell datacell;
            
             table = new PdfPTable(2);
            table.setWidths(new int[]{3, 2});
            table.setWidthPercentage(100);

            cell = new PdfPCell(new Phrase("\nNo. "+rs.getString("sanction_no"), f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
           
            cell = new PdfPCell(new Phrase("Dated:"+CommonFunctions.getFormattedOutputDate1(rs.getDate("sanction_date")), f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
             document.add(table);

            Users ue = getLoanEmpDetail(rs.getString("issue_letter_login_id"));

            String pointStr1 = "From\n          " + ue.getFullName() + ",\n          " + ue.getPostname() + "\nTo\n          The Accountant General (A & E), Odisha,\n          Bhubaneswar.";
            Paragraph point2 = new Paragraph(pointStr1, pFont);
            document.add(point2);
            String pointStr2 = "Sub:  Sanction of advance for purchase of " + rs.getString("loan_type") + " in favour of 1(one) employees of\n          " + rs.getString("office_name");
            Paragraph point3 = new Paragraph(pointStr2, pFont);
            document.add(point3);
            //Chunk c1 = new Chunk(rs.getInt("release_amount"), ):
            String pointStr = "Sir,\n          I am directed to convey the sanction of the Governor under Rules 237-253 of the Odisha General Financial Rules, Volume-I read "
                    + "with Financial Department O.M. No. dated, No., dated, No. dt. and No. dt. to the payment of an advance of Rs." + rs.getInt("release_amount") + "/- (" + CommonFunctions.convertNumber(rs.getInt("release_amount")) + ") "
                    + "only in favour of 1(one) employees of this Department as per the statement enclosed @" + rs.getInt("release_amount") + "/- each for the purpose of the "
                    + "purchase of " + rs.getString("loan_type") + ".";
            Paragraph point1 = new Paragraph(pointStr, pFont);
            document.add(point1);

            String pointStr4 = "\n2. The sanction is subject to availability of funds and will remain valid for one month from the date of issue.";
            Paragraph point4 = new Paragraph(pointStr4, pFont);
            document.add(point4);

            String msg1 = "\n3. The employees concerned are being informed that he will have to pay simple interest at the rate of " + rs.getInt("rate_interest") + "% per annum or at such rate of interest as fixed by Government from time to time in respect of this advance. The interest will be calculated on the balance outstanding on the last day of each month. The amount of interest thus calculated will be recovered in " + rs.getInt("prinicipal_installment") + "(" + CommonFunctions.convertNumber(rs.getInt("prinicipal_installment")) + ") instalments after the whole of the principal amount is recovered.";
            Paragraph point5 = new Paragraph(msg1, pFont);
            document.add(point5);

            String chatOfAccount = rs.getString("chat_account");
            String demandNo = chatOfAccount.substring(0, 2);
            String majorHead = chatOfAccount.substring(2, 6);
            String etc = chatOfAccount.substring(8, 11);

            String msg2 = "\n4. The charge is debitable to Demand No. " + demandNo + " - " + majorHead + " - Loans to Government Servants etc. - " + etc + " - Advances "
                    + "for purchase of the Computers - " + chatOfAccount.substring(11, 15) + " - Loans & Advances - " + chatOfAccount.substring(15, 20)
                    + " - " + rs.getString("loan_type") + " Advances Non-plan\" during the financial year '" + CommonFunctions.getFinancialYear(rs.getString("sanction_date")) + "'.";
            Paragraph point6 = new Paragraph(msg2, pFont);
            document.add(point6);

            String msg3 = "\n5. The advance is sanctioned subject to the condition that no such previous advance is outstanding against the officers/employees concerned.";
            Paragraph point7 = new Paragraph(msg3, pFont);
            document.add(point7);

            String sql = "select post, off_en from g_office GO INNER JOIN g_post GP ON GO.ddo_post = GP.post_code WHERE GO.off_code = ?";
            ps1 = con.prepareStatement(sql);
            ps1.setString(1, rs.getString("ddo_code") + "0000");
            rs1 = ps1.executeQuery();
            rs1.next();
            String ddoDesignation = rs1.getString("post");
            String msg4 = "\n6. The " + ddoDesignation + " to Government, " + rs1.getString("off_en") + " (Accounts) is the Drawing and Disbursing Officer in respect of the advance.";
            Paragraph point8 = new Paragraph(msg4, pFont);
            document.add(point8);

            String msg5 = "\n7. The recovery of the advance will commence with the first drawal of pay after the advance is drawn.";
            Paragraph point9 = new Paragraph(msg5, pFont);
            document.add(point9);

            String msg6 = "\n8. The statement enclosed includes required information ie. name with designation, basic pay, Grade Pay, Loanee identity (GPF) No., date of birth, rate of recovery and number of instalments etc. of the employees concerned.";
            Paragraph point10 = new Paragraph(msg6, pFont);
            document.add(point10);

            Paragraph blank = new Paragraph("\nYours Faithfully     \n\n\n" + ue.getPostname() + " to Government\n\n", bottomRule);
            blank.setAlignment(Element.ALIGN_RIGHT);
            document.add(blank);

            PdfPTable table3 = new PdfPTable(9);
            table3.setWidths(new int[]{2, 12, 5, 5, 5, 5, 5, 5, 5});
            table3.setWidthPercentage(100);
            PdfPCell datacell3;

            datacell3 = new PdfPCell(new Paragraph(rs.getString("loan_type") + " Advance is sanctioned during " + fyear, dataHdrFont));
            datacell3.setBorder(Rectangle.NO_BORDER);
            datacell3.setColspan(9);
            datacell3.setFixedHeight(15);
            datacell3.setHorizontalAlignment(Element.ALIGN_LEFT);
            table3.addCell(datacell3);

            datacell3 = new PdfPCell(new Phrase("SL No ", hdrTextFont1));
            datacell3.setHorizontalAlignment(Element.ALIGN_LEFT);
            table3.addCell(datacell3);

            datacell3 = new PdfPCell(new Phrase("Name & Designation ", hdrTextFont1));
            datacell3.setHorizontalAlignment(Element.ALIGN_LEFT);
            table3.addCell(datacell3);

            datacell3 = new PdfPCell(new Phrase("D.O.B", hdrTextFont1));
            datacell3.setHorizontalAlignment(Element.ALIGN_LEFT);
            table3.addCell(datacell3);

            datacell3 = new PdfPCell(new Phrase("Present pay + Grade Pay", hdrTextFont1));
            datacell3.setHorizontalAlignment(Element.ALIGN_LEFT);
            table3.addCell(datacell3);

            datacell3 = new PdfPCell(new Phrase("Temp./Perm.", hdrTextFont1));
            datacell3.setHorizontalAlignment(Element.ALIGN_LEFT);
            table3.addCell(datacell3);

            datacell3 = new PdfPCell(new Phrase("Suretybond ", hdrTextFont1));
            datacell3.setHorizontalAlignment(Element.ALIGN_LEFT);
            table3.addCell(datacell3);

            datacell3 = new PdfPCell(new Phrase("No.of instalments ", hdrTextFont1));
            datacell3.setHorizontalAlignment(Element.ALIGN_LEFT);
            table3.addCell(datacell3);

            datacell3 = new PdfPCell(new Phrase("Amount per instalment ", hdrTextFont1));
            datacell3.setHorizontalAlignment(Element.ALIGN_LEFT);
            table3.addCell(datacell3);

            datacell3 = new PdfPCell(new Phrase("Identity No", hdrTextFont1));
            datacell3.setHorizontalAlignment(Element.ALIGN_LEFT);
            table3.addCell(datacell3);
            //table3.addCell(datacell3);

            datacell3 = new PdfPCell(new Phrase("1", dataValFont));
            datacell3.setHorizontalAlignment(Element.ALIGN_LEFT);
            table3.addCell(datacell3);

            datacell3 = new PdfPCell(new Phrase(rs.getString("name") + "\n" + rs.getString("designation"), dataValFont));
            datacell3.setHorizontalAlignment(Element.ALIGN_LEFT);
            table3.addCell(datacell3);

            datacell3 = new PdfPCell(new Phrase(CommonFunctions.getFormattedOutputDate1(rs.getDate("dob")), dataValFont));
            datacell3.setHorizontalAlignment(Element.ALIGN_LEFT);
            table3.addCell(datacell3);

            datacell3 = new PdfPCell(new Phrase(rs.getInt("gp") + "", dataValFont));
            datacell3.setHorizontalAlignment(Element.ALIGN_LEFT);
            table3.addCell(datacell3);

            datacell3 = new PdfPCell(new Phrase(rs.getString("emptype"), dataValFont));
            datacell3.setHorizontalAlignment(Element.ALIGN_LEFT);
            table3.addCell(datacell3);

            datacell3 = new PdfPCell(new Phrase("Not applicable", dataValFont));
            datacell3.setHorizontalAlignment(Element.ALIGN_LEFT);
            table3.addCell(datacell3);

            datacell3 = new PdfPCell(new Phrase(rs.getInt("prinicipal_installment_amt") + "", dataValFont));
            datacell3.setHorizontalAlignment(Element.ALIGN_LEFT);
            table3.addCell(datacell3);

            datacell3 = new PdfPCell(new Phrase(rs.getInt("release_amount") + "", dataValFont));
            datacell3.setHorizontalAlignment(Element.ALIGN_LEFT);
            table3.addCell(datacell3);

            datacell3 = new PdfPCell(new Phrase(rs.getString("gpfno"), dataValFont));
            datacell3.setHorizontalAlignment(Element.ALIGN_LEFT);
            table3.addCell(datacell3);

            document.add(table3);

            /*

             PdfPTable table2 = new PdfPTable(2);
             table2.setWidths(new int[]{5, 5});
             table2.setWidthPercentage(100);
             PdfPCell datacell2;

             datacell2 = new PdfPCell(new Paragraph(" ", dataHdrFont));
             datacell2.setBorder(Rectangle.NO_BORDER);
             datacell2.setColspan(2);
             datacell2.setFixedHeight(15);
             datacell2.setHorizontalAlignment(Element.ALIGN_LEFT);
             table2.addCell(datacell2);

             datacell2 = new PdfPCell(new Phrase("Memo No: " + "", hdrTextFont1));
             datacell2.setBorder(Rectangle.NO_BORDER);
             datacell2.setHorizontalAlignment(Element.ALIGN_LEFT);
             table2.addCell(datacell2);

             datacell2 = new PdfPCell(new Phrase("Memo Date: " + "", hdrTextFont1));
             datacell2.setHorizontalAlignment(Element.ALIGN_LEFT);
             datacell2.setBorder(Rectangle.NO_BORDER);
             table2.addCell(datacell2);

             datacell2 = new PdfPCell(new Phrase("1. Copy alonwith copy of the statement forwarded to Finance Department/Accounts Section(in duplicate )/ Treasury Officer, Special Treasury, No.-II OLA Campus, Bhuabneswar / Person Concerned / Personal File / G.F For information and necessary action. The employees concerned are requested to kindly ensure that the computer is purchased and mortgaged to Government in time and watch the recovery of advance along with interest.", dataValFont));
             datacell2.setHorizontalAlignment(Element.ALIGN_LEFT);
             datacell2.setColspan(2);
             datacell2.setBorder(Rectangle.NO_BORDER);
             table2.addCell(datacell2);

             datacell2 = new PdfPCell(new Phrase("2. The advance is sanctioned on the undertaking that a Computer purchased and the amount taken as advance is limited to the price paid for it including such items as cost of taking delivery and subsequent repairs.But the advance must not be drawn from the Treasury untill it is actually needed for the purchase of the " + rs.getString("loan_type") + ". The date of drawal of the advance and the date of purchase should be reported to Finance Department as soon as they are done.", dataValFont));
             datacell2.setHorizontalAlignment(Element.ALIGN_LEFT);
             datacell2.setColspan(2);
             datacell2.setBorder(Rectangle.NO_BORDER);
             table2.addCell(datacell2);

             datacell2 = new PdfPCell(new Phrase("3. The employees concerned should execute agreement in FORM-19 of the O.G.F.R Volume-II and furnish the same to the concerned Drawing and Disbursing Officer before drawal of the advance. THE D.D.O should scrutinize the same and record a certificate of execution of agreement of the bill and after disbursement of the advance, return the agreement of the bill and after disbursement of the advance, return the agreement to the sanctioning authority indicating the date of drawal and disbursement.The later on receipt of agreement shall scrutinize the same and thereafter inform the Accountant General, Odisha that the sanctioned advance has been paid to the concerned officer on execution of proper agreement.", dataValFont));
             datacell2.setHorizontalAlignment(Element.ALIGN_LEFT);
             datacell2.setColspan(2);
             datacell2.setBorder(Rectangle.NO_BORDER);
             table2.addCell(datacell2);

             datacell2 = new PdfPCell(new Phrase("4. A Mortgage Bond in the prescribed form should be executed by the employee concerned immediately after the " + rs.getString("loan_type") + " is purchased.The bond should be submitted to the Finance Department, within one month from the date on which the advance is drawn for safe custody.A full specification of the conveyance, purchased given at full cost price should be entered in the schedule to the mortgage bond which is no circumstances be omitted or left incomplete.", dataValFont));
             datacell2.setHorizontalAlignment(Element.ALIGN_LEFT);
             datacell2.setColspan(2);
             datacell2.setBorder(Rectangle.NO_BORDER);
             table2.addCell(datacell2);

             datacell2 = new PdfPCell(new Phrase("5. Contravention of those orders will render the Government servant liable to refund the whole of the amount advance with interest accrued, unless good reason is shown to the contrary. The amount to be refunded must be recovered in not more than three consecutive monthly instalments.", dataValFont));
             datacell2.setHorizontalAlignment(Element.ALIGN_LEFT);
             datacell2.setColspan(2);
             datacell2.setBorder(Rectangle.NO_BORDER);
             table2.addCell(datacell2);

             datacell2 = new PdfPCell(new Paragraph(" ", dataHdrFont));
             datacell2.setBorder(Rectangle.NO_BORDER);
             datacell2.setColspan(2);
             datacell2.setFixedHeight(15);
             datacell2.setHorizontalAlignment(Element.ALIGN_LEFT);
             table2.addCell(datacell2);

             datacell2 = new PdfPCell(new Phrase("letterfromname", dataValFont));
             datacell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
             datacell2.setColspan(2);
             datacell2.setBorder(Rectangle.NO_BORDER);
             table2.addCell(datacell2);
             datacell2 = new PdfPCell(new Paragraph(" ", dataHdrFont));
             datacell2.setBorder(Rectangle.NO_BORDER);
             datacell2.setColspan(2);
             datacell2.setFixedHeight(15);
             datacell2.setHorizontalAlignment(Element.ALIGN_LEFT);
             table2.addCell(datacell2);

             document.add(table2);*/
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void saveLoanGPFsaction(LoanGPFForm lform) {
        Connection con = null;
        Calendar now = Calendar.getInstance();
        Date loanapplyDate = now.getTime();

        PreparedStatement ps = null;
        ResultSet rs = null;
        Date dt1 = null;
        String pending_at = null;
        String pending_spc = null;
        int taskid = lform.getTaskid();
        int loanid = lform.getLoanId();

        String letterno = lform.getLetterNo();
        String letterdate = lform.getLetterDate();
        String letterform = lform.getLetterformName();
        String letterto = lform.getLetterto();
        String letterdesi = lform.getLetterformdesignation();
        String memono = lform.getMemoNo();
        String memodate = lform.getMemoDate();

        MultipartFile loanAttch = lform.getFile_att();
        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("UPDATE hw_emp_gpf_loan SET letterno=?,letterdate=?,letterfrom=?,fromdesignation=?,letterto=?,memono=?,memodate=? WHERE taskid=? AND loan_gpf_id=?");
            ps.setString(1, letterno);
            ps.setString(2, letterdate);
            ps.setString(3, letterform);
            ps.setString(4, letterdesi);
            ps.setString(5, letterto);
            ps.setString(6, memono);
            ps.setString(7, memodate);
            ps.setInt(8, taskid);
            ps.setInt(9, loanid);
            int stsTask = ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    /**
     * ******************************* Temp GPF LOAN APPLY PANEL
     * *******************
     */
    @Override
    public LoanTempGPFForm TempGPFEmpDetails(String hrmsid) {
        LoanTempGPFForm loanvalue = new LoanTempGPFForm();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Calendar now = Calendar.getInstance();
        int cmonth = now.get(Calendar.MONTH);
        int cyear = now.get(Calendar.YEAR);
        int previousYear = cyear;
        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("SELECT a.gpf_no,a.gp,a.emp_id,ARRAY_TO_STRING(ARRAY[INITIALS, a.F_NAME, a.M_NAME,"
                    + "a.L_NAME], ' ') fullname,a.cur_basic_salary,to_char(a.JOINDATE_OF_GOO, 'DD-Mon-YYYY') as doj,"
                    + "to_char(a.dos, 'DD-Mon-YYYY') as dos,b.spn,b.spc,b.gpc,c.post,d.department_name,"
                    + "EXTRACT(YEAR from AGE(NOW(), a.JOINDATE_OF_GOO)) as age,EXTRACT(YEAR from AGE(a.dos,NOW())) as remainingage,e.ddo_code,e.off_en  FROM emp_mast a,g_spc b,g_post c,"
                    + "g_department d,g_office e WHERE  a.cur_spc=b.spc AND b.dept_code=d.department_code AND  "
                    + " b.gpc=c.post_code AND a.cur_off_code=e.off_code AND emp_id=?");
            ps.setString(1, hrmsid);
            rs = ps.executeQuery();
            if (rs.next()) {
                loanvalue.setEmpName(rs.getString("fullname"));
                String gpfno = rs.getString("gpf_no");
                loanvalue.setEmpSPC(rs.getString("spc"));
                String deptname = rs.getString("department_name");
                String accountNo = gpfno + " " + deptname;
                loanvalue.setGpfno(accountNo);

                String basicsalary_gp = rs.getString("cur_basic_salary") + "+ GP " + rs.getString("gp");
                loanvalue.setDesignation(rs.getString("post"));
                loanvalue.setPay(basicsalary_gp);
                loanvalue.setDoj(rs.getString("doj"));

                if (cmonth < 4) {
                    previousYear = cyear - 1;
                    cyear = previousYear;

                }
                if (cmonth == 0) {
                    cmonth = 12;
                }

                String ddo_code = rs.getString("ddo_code");
                String ddo_office = rs.getString("off_en");
                String ddooffice = ddo_office + "(" + ddo_code + ")";

                loanvalue.setDdocode(ddo_code);

            }
            /*  ps = con.prepareStatement("SELECT SUM(ad_amt) as total_subscription FROM aq_dtls WHERE ad_code='GPF' AND  emp_code='" + hrmsid + "' AND (p_month BETWEEN 4 AND '" + cmonth + "' AND (p_year  BETWEEN  '" + previousYear + "' AND  '" + cyear + "')) ");
             rs = ps.executeQuery();
             String total_subscription = null;
             if (rs.next()) {
             total_subscription = rs.getString("total_subscription");
             }
             */

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return loanvalue;
    }

    public void savetempgpfLoanData(LoanTempGPFForm lform, String empid, String filepath) {
        Connection con = null;
        Calendar now = Calendar.getInstance();
        Date loanapplyDate = now.getTime();

        PreparedStatement ps = null;
        ResultSet rs = null;
        Date dt1 = null;
        Date dt2 = null;
        Date dt3 = null;

        DateFormat cdate = new SimpleDateFormat("dd-MMM-yyyy");
        MultipartFile loanAttch = lform.getFile_att();
        try {
            con = this.dataSource.getConnection();
            String forwardId = lform.getForwardtoHrmsid();
            String hidSpc = lform.getHidSPC();
            int loanvalue = 49;
            String initiatedSpc = lform.getEmpSPC();
            // String loanapplyfor=lform.getLoanapplyfor();
            String session_empId = empid;

            String amount_credit = lform.getAmount_credit();
            String amount_subscription = lform.getAmount_subscription();
            String deduct_adv = lform.getDeduct_adv();
            String advance_taken = lform.getAdvance_taken();
            String amount_drawal = lform.getAmount_drawal();
            String date_drawal = lform.getDate_drawal();
            String purpose_drawal = lform.getPurpose_drawal();
            String date_repaid = lform.getDate_repaid();
            String date_drawal_sanction = lform.getDate_drawal_sanction();
            String date_drawal_cadvance = lform.getDate_drawal_cadvance();
            String balance_outstanding = lform.getBalance_outstanding();
            String rate_recovery = lform.getRate_recovery();

            String final_payment = lform.getFinal_payment();
            String amount_adv = lform.getAmount_adv();
            String purpose = lform.getPurpose();
            String total_advance = lform.getTotal_advance();
            String noofinst = lform.getNoofinst();
            String gpfno = lform.getGpfno();
            String gppftype = "TEMP GPF Advance";

            String ddo = lform.getAccountOfficer();

            String strTaskmaster = "INSERT INTO task_master (process_id "
                    + ",initiated_by "
                    + ",status_id "
                    + ",pending_at "
                    + ",apply_to "
                    + ",initiated_spc "
                    + ",pending_spc "
                    + ",apply_to_spc "
                    + ",initiated_on "
                    + ") values(?,?,?,?,?,?,?,?,?)";
            //ps = con.prepareStatement(strTaskmaster);
            ps = con.prepareStatement(strTaskmaster, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, 10);
            ps.setString(2, session_empId);
            ps.setInt(3, loanvalue);
            ps.setString(4, forwardId);
            ps.setString(5, forwardId);
            ps.setString(6, initiatedSpc);
            ps.setString(7, hidSpc);
            ps.setString(8, hidSpc);
            ps.setTimestamp(9, new Timestamp(loanapplyDate.getTime()));

            int stsTask = ps.executeUpdate();
            rs = ps.getGeneratedKeys();
            rs.next();
            int taskId = rs.getInt("TASK_ID");

            if (stsTask == 1) {
                String str = "INSERT INTO hw_emp_temp_gpf_loan(taskid "
                        + ",emp_id "
                        + ",amount_credit"
                        + ",amount_subscription"
                        + ",deduct_adv"
                        + ",advance_taken"
                        + ",amount_drawal"
                        + ",date_drawal"
                        + ",purpose_drawal"
                        + ",date_repaid"
                        + ",date_drawal_sanction"
                        + ",date_drawal_cadvance"
                        + ",balance_outstanding"
                        + ",rate_recovery"
                        + ",final_payment"
                        + ",amount_adv"
                        + ",purpose"
                        + ",total_advance"
                        + ",noofinst"
                        + ",ddo_code"
                        + ",forward_to"
                        + ",loan_apply_date"
                        + ",loan_status,gpftype) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                ps = con.prepareStatement(str);
                ps.setInt(1, taskId);
                ps.setString(2, session_empId);
                if (amount_credit != null && !amount_credit.equals("")) {
                    ps.setDouble(3, Double.parseDouble(amount_credit));
                } else {
                    ps.setDouble(3, 0);
                }
                if (amount_subscription != null && !amount_subscription.equals("")) {
                    ps.setDouble(4, Double.parseDouble(amount_subscription));
                } else {
                    ps.setDouble(4, 0);
                }

                if (deduct_adv != null && !deduct_adv.equals("")) {
                    ps.setDouble(5, Double.parseDouble(deduct_adv));
                } else {
                    ps.setDouble(5, 0);
                }
                ps.setString(6, advance_taken);
                if (amount_drawal != null && !amount_drawal.equals("")) {
                    ps.setDouble(7, Double.parseDouble(amount_drawal));
                } else {
                    ps.setDouble(7, 0);
                }

                ps.setString(8, date_drawal);
                ps.setString(9, purpose_drawal);
                ps.setString(10, date_repaid);
                ps.setString(11, date_drawal_sanction);

                ps.setString(12, date_drawal_cadvance);
                ps.setString(13, balance_outstanding);
                ps.setString(14, rate_recovery);
                ps.setString(15, final_payment);
                if (amount_adv != null && !amount_adv.equals("")) {
                    ps.setDouble(16, Double.parseDouble(amount_adv));
                } else {
                    ps.setDouble(16, 0);
                }
                ps.setString(17, purpose);

                if (total_advance != null && !total_advance.equals("")) {
                    ps.setDouble(18, Double.parseDouble(total_advance));
                } else {
                    ps.setDouble(18, 0);
                }
                ps.setString(19, noofinst);
                ps.setString(20, ddo);

                ps.setString(21, hidSpc);
                ps.setTimestamp(22, new Timestamp(loanapplyDate.getTime()));
                ps.setInt(23, loanvalue);
                ps.setString(24, gppftype);

                int sts = ps.executeUpdate();
                if (sts > 0) {
                    InputStream inputStream = null;
                    OutputStream outputStream = null;
                    String filename = "";
                    if (loanAttch != null && !loanAttch.isEmpty()) {
                        long time = System.currentTimeMillis();
                        filename = session_empId + "_" + taskId + "_" + time;

                        ps = con.prepareStatement("UPDATE hw_emp_temp_gpf_loan SET original_filename=?,disk_file_name=?,file_type=? WHERE TASKID=?");
                        ps.setString(1, loanAttch.getOriginalFilename());
                        ps.setString(2, filename);
                        ps.setString(3, loanAttch.getContentType());
                        ps.setInt(4, taskId);
                        ps.executeUpdate();

                        String dirpath = filepath + "/";
                        File newfile = new File(dirpath);
                        if (!newfile.exists()) {
                            newfile.mkdirs();
                        }

                        outputStream = new FileOutputStream(dirpath + filename);
                        int read = 0;
                        byte[] bytes = new byte[1024];
                        inputStream = loanAttch.getInputStream();
                        while ((read = inputStream.read(bytes)) != -1) {
                            outputStream.write(bytes, 0, read);
                        }
                    }

                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public LoanTempGPFForm tempgpfLoanDetails(int taskid) {

        LoanTempGPFForm loanvalue = new LoanTempGPFForm();
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement pstask = null;
        PreparedStatement psapply = null;
        ResultSet rs = null;
        ResultSet rstask = null;
        ResultSet rsapply = null;
        Calendar now = Calendar.getInstance();
        int cmonth = now.get(Calendar.MONTH);
        int cyear = now.get(Calendar.YEAR);
        int previousYear = cyear;
        String empId = null;

        try {
            con = this.dataSource.getConnection();
            pstask = con.prepareStatement("SELECT a.*,b.* FROM hw_emp_temp_gpf_loan a,task_master b WHERE a.taskid=b.task_id AND a.taskid='" + taskid + "' ");
            rstask = pstask.executeQuery();

            if (rstask.next()) {
                empId = rstask.getString("emp_id");

                loanvalue.setEmpId(rstask.getString("emp_id"));

                loanvalue.setAmount_credit(rstask.getString("amount_credit"));
                loanvalue.setAmount_subscription(rstask.getString("amount_subscription"));
                loanvalue.setDeduct_adv(rstask.getString("deduct_adv"));
                loanvalue.setAdvance_taken(rstask.getString("advance_taken"));
                loanvalue.setAmount_drawal(rstask.getString("amount_drawal"));
                loanvalue.setDate_drawal(rstask.getString("date_drawal"));
                loanvalue.setPurpose_drawal(rstask.getString("purpose_drawal"));
                loanvalue.setDate_repaid(rstask.getString("date_repaid"));
                loanvalue.setDate_drawal_sanction(rstask.getString("date_drawal_sanction"));
                loanvalue.setDate_drawal_cadvance(rstask.getString("date_drawal_cadvance"));
                loanvalue.setBalance_outstanding(rstask.getString("balance_outstanding"));
                loanvalue.setRate_recovery(rstask.getString("rate_recovery"));
                loanvalue.setFinal_payment(rstask.getString("final_payment"));
                loanvalue.setAmount_adv(rstask.getString("amount_adv"));
                loanvalue.setPurpose(rstask.getString("purpose"));
                loanvalue.setTotal_advance(rstask.getString("total_advance"));

                loanvalue.setNoofinst(rstask.getString("noofinst"));

                loanvalue.setApprovedBy(rstask.getString("apply_to"));
                loanvalue.setApprovedSpc(rstask.getString("pending_spc"));
                loanvalue.setLoanId(rstask.getInt("loan_temp_gpf_id"));
                loanvalue.setTaskid(rstask.getInt("taskid"));
                loanvalue.setLoancomments(rstask.getString("comments"));
                loanvalue.setStatusId(rstask.getInt("status_id"));

            }

            ps = con.prepareStatement("SELECT a.gpf_no,a.gp,a.emp_id,ARRAY_TO_STRING(ARRAY[INITIALS, a.F_NAME, a.M_NAME,"
                    + "a.L_NAME], ' ') fullname,a.cur_basic_salary,to_char(a.JOINDATE_OF_GOO, 'DD-Mon-YYYY') as doj,"
                    + "to_char(a.dos, 'DD-Mon-YYYY') as dos,b.spn,b.spc,b.gpc,c.post,d.department_name,"
                    + "EXTRACT(YEAR from AGE(NOW(), a.JOINDATE_OF_GOO)) as age,EXTRACT(YEAR from AGE(a.dos,NOW())) as remainingage,e.ddo_code,e.off_en  FROM emp_mast a,g_spc b,g_post c,"
                    + "g_department d,g_office e WHERE  a.cur_spc=b.spc AND b.dept_code=d.department_code AND  "
                    + " b.gpc=c.post_code AND a.cur_off_code=e.off_code AND emp_id=?");
            ps.setString(1, empId);

            rs = ps.executeQuery();
            if (rs.next()) {
                loanvalue.setEmpName(rs.getString("fullname"));

                String gpfno = rs.getString("gpf_no");
                loanvalue.setEmpSPC(rs.getString("spc"));
                String deptname = rs.getString("department_name");
                String accountNo = gpfno + " " + deptname;
                loanvalue.setGpfno(accountNo);

                String basicsalary_gp = rs.getString("cur_basic_salary") + "+ GP " + rs.getString("gp");
                loanvalue.setDesignation(rs.getString("post"));
                loanvalue.setPay(basicsalary_gp);
                loanvalue.setDoj(rs.getString("doj"));

                if (cmonth < 4) {
                    previousYear = cyear - 1;
                    cyear = previousYear;

                }
                if (cmonth == 0) {
                    cmonth = 12;
                }

                String ddo_code = rs.getString("ddo_code");
                String ddo_office = rs.getString("off_en");
                String ddooffice = ddo_office + "(" + ddo_code + ")";

                loanvalue.setDdocode(ddo_code);
                if (rstask.getString("disk_file_name") != null && !rstask.getString("disk_file_name").equals("")) {
                    loanvalue.setDiskFileName(rstask.getString("disk_file_name"));
                } else {
                    loanvalue.setDiskFileName("");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return loanvalue;

    }

    @Override
    public void DownloadtempgpfLoanAttch(HttpServletResponse response, String filepath, String loanid) throws IOException {

        Connection con = null;

        Statement stmt = null;
        ResultSet rs = null;
        int BUFFER_LENGTH = 4096;
        OutputStream out = response.getOutputStream();
        try {
            con = dataSource.getConnection();

            stmt = con.createStatement();
            String sql = "SELECT * FROM hw_emp_temp_gpf_loan WHERE loan_temp_gpf_id='" + loanid + "'";
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                File f = null;
                String dirpath = filepath + "/" + rs.getString("DISK_FILE_NAME");
                f = new File(dirpath);
                if (f.exists()) {
                    String originalFilename = rs.getString("ORIGINAL_FILENAME");
                    String filetype = rs.getString("FILE_TYPE");

                    response.setContentLength((int) f.length());
                    FileInputStream is = new FileInputStream(f);

                    response.setContentType(filetype);
                    response.setHeader("Content-Disposition", "attachment; filename=\"" + originalFilename + "\"");

                    byte[] bytes = new byte[BUFFER_LENGTH];
                    int read = 0;
                    while ((read = is.read(bytes, 0, BUFFER_LENGTH)) != -1) {
                        out.write(bytes, 0, read);
                    }
                    is.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, stmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    public void savetempfpfApproveData(LoanTempGPFForm lform) {
        Connection con = null;
        Calendar now = Calendar.getInstance();
        Date loanapplyDate = now.getTime();

        PreparedStatement ps = null;
        ResultSet rs = null;
        Date dt1 = null;
        String pending_at = null;
        String pending_spc = null;
        int taskid = lform.getTaskid();
        int loanid = lform.getLoanId();
        int loanstatus = lform.getLoan_status();
        String Comments = lform.getLoancomments();
        String approvedby = lform.getApprovedBy();
        String approvedspc = lform.getApprovedSpc();

        DateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");

        MultipartFile loanAttch = lform.getFile_att();
        try {
            con = this.dataSource.getConnection();
            if (loanstatus == 51 || loanstatus == 53) {
                String forwardId = lform.getForwardtoHrmsid();
                String hidSpc = lform.getHidSPC();
                ps = con.prepareStatement(" SELECT * FROM task_master WHERE  task_id='" + taskid + "'");
                rs = ps.executeQuery();
                while (rs.next()) {
                    pending_at = (rs.getString("pending_at"));
                    pending_spc = (rs.getString("pending_spc"));
                }

                ps = con.prepareStatement("UPDATE task_master SET status_id=?,pending_at=?,note=?,apply_to=?,pending_spc=?,apply_to_spc=? WHERE task_id=?");
                ps.setInt(1, loanstatus);
                ps.setString(2, forwardId);
                ps.setString(3, Comments);
                ps.setString(4, forwardId);
                ps.setString(5, hidSpc);
                ps.setString(6, hidSpc);
                ps.setInt(7, taskid);
                int stsTask = ps.executeUpdate();
                if (stsTask == 1) {
                    String str = "INSERT INTO workflow_log(task_id "
                            + ",task_action_date "
                            + ",action_taken_by"
                            + ",spc_ontime"
                            + ",task_status_id"
                            + ",note"
                            + ",forward_to"
                            + ",forwarded_spc"                           
                            + ",ref_id"
                            + ") values(?,?,?,?,?,?,?,?,?)";
                    ps = con.prepareStatement(str);
                    ps.setInt(1, taskid);
                    ps.setTimestamp(2, new Timestamp(loanapplyDate.getTime()));
                    ps.setString(3, pending_at);
                    ps.setString(4, pending_spc);
                    ps.setInt(5, loanstatus);
                    ps.setString(6, Comments);
                    ps.setString(7, forwardId);
                    ps.setString(8, hidSpc);
                    ps.setInt(9, loanid);
                   // int wfcode = CommonFunctions.getMaxCodeInteger("workflow_log", "log_id", con);
                   // ps.setInt(9, wfcode);
                    ps.executeUpdate();

                    ps = con.prepareStatement("UPDATE hw_emp_temp_gpf_loan SET loan_status=?,comments=? WHERE taskid=? AND loan_temp_gpf_id=?");
                    ps.setInt(1, loanstatus);
                    ps.setString(2, Comments);
                    ps.setInt(3, taskid);
                    ps.setInt(4, loanid);
                    ps.executeUpdate();

                }

            } else {
                ps = con.prepareStatement("UPDATE hw_emp_temp_gpf_loan SET loan_status=?,approved_by=?,approved_spc=?,comments=?,loan_sanction_date=? WHERE taskid=? AND loan_temp_gpf_id=?");
                ps.setInt(1, loanstatus);
                ps.setString(2, approvedby);
                ps.setString(3, approvedspc);
                ps.setString(4, Comments);
                ps.setTimestamp(5, new Timestamp(loanapplyDate.getTime()));
                ps.setInt(6, taskid);
                ps.setInt(7, loanid);
                int stsTask = ps.executeUpdate();

                if (stsTask == 1) {
                    ps = con.prepareStatement("UPDATE task_master SET status_id=?,pending_at=?,note=?,apply_to=?,pending_spc=?,apply_to_spc=? WHERE task_id=?");
                    ps.setInt(1, loanstatus);
                    ps.setString(2, null);
                    ps.setString(3, Comments);
                    ps.setString(4, null);
                    ps.setString(5, null);
                    ps.setString(6, null);
                    ps.setInt(7, taskid);
                    ps.executeUpdate();
                }    
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    public LoanTempGPFForm Reapplytempgpf(String option, String hrmsid, int loanid) {

        LoanTempGPFForm loanvalue = new LoanTempGPFForm();
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement pstask = null;
        PreparedStatement psapply = null;
        ResultSet rs = null;
        ResultSet rstask = null;
        ResultSet rsapply = null;
        Calendar now = Calendar.getInstance();
        int cmonth = now.get(Calendar.MONTH);
        int cyear = now.get(Calendar.YEAR);
        int previousYear = cyear;

        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("SELECT a.gpf_no,a.gp,a.emp_id,ARRAY_TO_STRING(ARRAY[INITIALS, a.F_NAME, a.M_NAME,"
                    + "a.L_NAME], ' ') fullname,a.cur_basic_salary,to_char(a.JOINDATE_OF_GOO, 'DD-Mon-YYYY') as doj,"
                    + "to_char(a.dos, 'DD-Mon-YYYY') as dos,b.spn,b.spc,b.gpc,c.post,d.department_name,"
                    + "EXTRACT(YEAR from AGE(NOW(), a.JOINDATE_OF_GOO)) as age,EXTRACT(YEAR from AGE(a.dos,NOW())) as remainingage,e.ddo_code,e.off_en  FROM emp_mast a,g_spc b,g_post c,"
                    + "g_department d,g_office e WHERE  a.cur_spc=b.spc AND b.dept_code=d.department_code AND  "
                    + " b.gpc=c.post_code AND a.cur_off_code=e.off_code AND emp_id=?");
            ps.setString(1, hrmsid);

            rs = ps.executeQuery();
            if (rs.next()) {
                loanvalue.setEmpName(rs.getString("fullname"));

                String gpfno = rs.getString("gpf_no");
                loanvalue.setEmpSPC(rs.getString("spc"));
                String deptname = rs.getString("department_name");
                String accountNo = gpfno + " " + deptname;
                loanvalue.setGpfno(accountNo);

                String basicsalary_gp = rs.getString("cur_basic_salary") + "+ GP " + rs.getString("gp");
                loanvalue.setDesignation(rs.getString("post"));
                loanvalue.setPay(basicsalary_gp);
                loanvalue.setDoj(rs.getString("doj"));

                if (cmonth < 4) {
                    previousYear = cyear - 1;
                    cyear = previousYear;

                }
                if (cmonth == 0) {
                    cmonth = 12;
                }

                String ddo_code = rs.getString("ddo_code");
                String ddo_office = rs.getString("off_en");
                String ddooffice = ddo_office + "(" + ddo_code + ")";

                loanvalue.setDdocode(ddo_code);

            }
            pstask = con.prepareStatement("SELECT a.*,b.* FROM hw_emp_temp_gpf_loan a,task_master b WHERE a.taskid=b.task_id AND a.loan_temp_gpf_id='" + loanid + "' ");
            rstask = pstask.executeQuery();

            if (rstask.next()) {

                loanvalue.setEmpId(rstask.getString("emp_id"));
                loanvalue.setApprovedBy(rstask.getString("apply_to"));
                loanvalue.setApprovedSpc(rstask.getString("pending_spc"));
                loanvalue.setLoanId(rstask.getInt("loan_temp_gpf_id"));
                loanvalue.setTaskid(rstask.getInt("taskid"));
                loanvalue.setLoancomments(rstask.getString("comments"));

                loanvalue.setAmount_credit(rstask.getString("amount_credit"));
                loanvalue.setAmount_subscription(rstask.getString("amount_subscription"));
                loanvalue.setDeduct_adv(rstask.getString("deduct_adv"));
                loanvalue.setAdvance_taken(rstask.getString("advance_taken"));
                loanvalue.setAmount_drawal(rstask.getString("amount_drawal"));
                loanvalue.setDate_drawal(rstask.getString("date_drawal"));
                loanvalue.setPurpose_drawal(rstask.getString("purpose_drawal"));
                loanvalue.setDate_repaid(rstask.getString("date_repaid"));
                loanvalue.setDate_drawal_sanction(rstask.getString("date_drawal_sanction"));
                loanvalue.setDate_drawal_cadvance(rstask.getString("date_drawal_cadvance"));
                loanvalue.setBalance_outstanding(rstask.getString("balance_outstanding"));
                loanvalue.setRate_recovery(rstask.getString("rate_recovery"));
                loanvalue.setFinal_payment(rstask.getString("final_payment"));
                loanvalue.setAmount_adv(rstask.getString("amount_adv"));
                loanvalue.setPurpose(rstask.getString("purpose"));
                loanvalue.setTotal_advance(rstask.getString("total_advance"));

                loanvalue.setNoofinst(rstask.getString("noofinst"));

                loanvalue.setApprovedBy(rstask.getString("apply_to"));
                loanvalue.setApprovedSpc(rstask.getString("pending_spc"));
                loanvalue.setLoanId(rstask.getInt("loan_temp_gpf_id"));
                loanvalue.setTaskid(rstask.getInt("taskid"));
                loanvalue.setLoancomments(rstask.getString("comments"));
                loanvalue.setStatusId(rstask.getInt("status_id"));
                if (rstask.getString("disk_file_name") != null && !rstask.getString("disk_file_name").equals("")) {
                    loanvalue.setDiskFileName(rstask.getString("disk_file_name"));
                } else {
                    loanvalue.setDiskFileName("");
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return loanvalue;

    }

    public void savetempgpfreapply(LoanTempGPFForm lform, String empid, String filepath) {
        Connection con = null;
        Calendar now = Calendar.getInstance();
        Date loanapplyDate = now.getTime();

        PreparedStatement ps = null;
        ResultSet rs = null;
        Date dt1 = null;
        Date dt2 = null;
        Date dt3 = null;

        DateFormat cdate = new SimpleDateFormat("dd-MMM-yyyy");
        MultipartFile loanAttch = lform.getFile_att();

        try {
            con = this.dataSource.getConnection();
            String forwardId = lform.getForwardtoHrmsid();
            String hidSpc = lform.getHidSPC();
            int loanvalue = 49;
            String initiatedSpc = lform.getEmpSPC();
            // String loanapplyfor=lform.getLoanapplyfor();
            String session_empId = empid;
            int taskid = lform.getTaskid();
            int loanid = lform.getLoanId();
            String Comments = lform.getLoancomments();
            String amount_credit = lform.getAmount_credit();
            String amount_subscription = lform.getAmount_subscription();
            String deduct_adv = lform.getDeduct_adv();
            String advance_taken = lform.getAdvance_taken();
            String amount_drawal = lform.getAmount_drawal();
            String date_drawal = lform.getDate_drawal();
            String purpose_drawal = lform.getPurpose_drawal();
            String date_repaid = lform.getDate_repaid();
            String date_drawal_sanction = lform.getDate_drawal_sanction();
            String date_drawal_cadvance = lform.getDate_drawal_cadvance();
            String balance_outstanding = lform.getBalance_outstanding();
            String rate_recovery = lform.getRate_recovery();

            String final_payment = lform.getFinal_payment();
            String amount_adv = lform.getAmount_adv();
            String purpose = lform.getPurpose();
            String total_advance = lform.getTotal_advance();
            String noofinst = lform.getNoofinst();
            String gpfno = lform.getGpfno();
            String gppftype = "TEMP GPF Advance";

            String strTaskmaster = "UPDATE   task_master SET status_id=?,pending_at=?,apply_to=?,pending_spc=?,apply_to_spc=?,initiated_on=?,note=? WHERE task_id=?";

            ps = con.prepareStatement(strTaskmaster);

            ps.setInt(1, loanvalue);
            ps.setString(2, forwardId);
            ps.setString(3, forwardId);

            ps.setString(4, hidSpc);
            ps.setString(5, hidSpc);
            ps.setTimestamp(6, new Timestamp(loanapplyDate.getTime()));
            ps.setString(7, "");
            ps.setInt(8, taskid);
            int stsTask = ps.executeUpdate();
            if (stsTask == 1) {
                String str = "UPDATE hw_emp_temp_gpf_loan SET amount_credit =?,amount_subscription=?,deduct_adv=?,advance_taken=?,amount_drawal=?,date_drawal=?,purpose_drawal=?,date_repaid=?,date_drawal_sanction=?  ";
                str = str + ",date_drawal_cadvance=?,balance_outstanding=?,rate_recovery=?,final_payment=?,amount_adv=?,purpose=?,total_advance=?";
                str = str + ",noofinst=?,forward_to=?,loan_status=?,comments=?";

                ps = con.prepareStatement(str);

                if (amount_credit != null && !amount_credit.equals("")) {
                    ps.setDouble(1, Double.parseDouble(amount_credit));
                } else {
                    ps.setDouble(1, 0);
                }
                if (amount_subscription != null && !amount_subscription.equals("")) {
                    ps.setDouble(2, Double.parseDouble(amount_subscription));
                } else {
                    ps.setDouble(2, 0);
                }

                if (deduct_adv != null && !deduct_adv.equals("")) {
                    ps.setDouble(3, Double.parseDouble(deduct_adv));
                } else {
                    ps.setDouble(3, 0);
                }
                ps.setString(4, advance_taken);
                if (amount_drawal != null && !amount_drawal.equals("")) {
                    ps.setDouble(5, Double.parseDouble(amount_drawal));
                } else {
                    ps.setDouble(5, 0);
                }

                ps.setString(6, date_drawal);
                ps.setString(7, purpose_drawal);
                ps.setString(8, date_repaid);
                ps.setString(9, date_drawal_sanction);

                ps.setString(10, date_drawal_cadvance);
                ps.setString(11, balance_outstanding);
                ps.setString(12, rate_recovery);
                ps.setString(13, final_payment);
                if (amount_adv != null && !amount_adv.equals("")) {
                    ps.setDouble(14, Double.parseDouble(amount_adv));
                } else {
                    ps.setDouble(14, 0);
                }
                ps.setString(15, purpose);

                if (total_advance != null && !total_advance.equals("")) {
                    ps.setDouble(16, Double.parseDouble(total_advance));
                } else {
                    ps.setDouble(16, 0);
                }
                ps.setString(17, noofinst);
                ps.setString(18, hidSpc);
                ps.setInt(19, loanvalue);

                ps.setString(20, Comments);

                int sts = ps.executeUpdate();
                if (sts > 0) {
                    InputStream inputStream = null;
                    OutputStream outputStream = null;
                    String filename = "";
                    if (loanAttch != null && !loanAttch.isEmpty()) {
                        long time = System.currentTimeMillis();
                        filename = session_empId + "_" + taskid + "_" + time;

                        ps = con.prepareStatement("UPDATE hw_emp_temp_gpf_loan SET original_filename=?,disk_file_name=?,file_type=? WHERE TASKID=?");
                        ps.setString(1, loanAttch.getOriginalFilename());
                        ps.setString(2, filename);
                        ps.setString(3, loanAttch.getContentType());
                        ps.setInt(4, taskid);
                        ps.executeUpdate();

                        String dirpath = filepath + "/";
                        File newfile = new File(dirpath);
                        if (!newfile.exists()) {
                            newfile.mkdirs();
                        }

                        outputStream = new FileOutputStream(dirpath + filename);
                        int read = 0;
                        byte[] bytes = new byte[1024];
                        inputStream = loanAttch.getInputStream();
                        while ((read = inputStream.read(bytes)) != -1) {
                            outputStream.write(bytes, 0, read);
                        }
                    }

                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }

    }

    @Override
    public void downloadLoanTypeI(Document document, int loanid, String hrmsid) {
        LoanForm loanvalue = new LoanForm();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        // PreparedStatement rstask = null;
        PreparedStatement psapply = null;
        PreparedStatement pstask = null;
        ResultSet rsapply = null;
        ResultSet rstask = null;

        Calendar now = Calendar.getInstance();
        int cmonth = now.get(Calendar.MONTH);
        int cyear = now.get(Calendar.YEAR);

        try {
            con = this.dataSource.getConnection();
            pstask = con.prepareStatement("SELECT a.*,to_char(a.date_drawal_adv, 'DD-Mon-YYYY') as date_drawal_format,to_char(a.date_commencement, 'DD-Mon-YYYY') as date_commencement_format,to_char(a.date_expire, 'DD-Mon-YYYY') as date_expire_format,b.* FROM hw_emp_loan a,task_master b WHERE a.taskid=b.task_id AND  a.loan_code=?");
            pstask.setInt(1, loanid);
            rstask = pstask.executeQuery();

            String apply_hrmsid = null;
            String ant_price = "";
            int no_installment = 0;
            String purchase_type = "";
            String amount_adv = "";
            int instalments = 0;
            String loan_availed = "";
            String motorcar_cycle_moped = "";
            String availed_amt_adv = "";
            String date_drawal_format = "";
            String principal_paid = "";
            String amount_standing = "";
            String officer_leave = "";
            String date_commencement_format = "";

            String date_expire_format = "";
            String apply_to = "";
            String pending_spc = "";
            String disk_file_name = "";
            String comments = "";
            String loan_type = "";
            String note = "";

            if (rstask.next()) {
                apply_hrmsid = rstask.getString("emp_id");
                ant_price = rstask.getString("ant_price");
                purchase_type = rstask.getString("purchase_type");
                amount_adv = rstask.getString("amount_adv");
                no_installment = rstask.getInt("no_installment");
                loanvalue.setPreviousAvail(rstask.getString("loan_availed"));
                loan_availed = rstask.getString("loan_availed");
                motorcar_cycle_moped = rstask.getString("motorcar_cycle_moped");
                availed_amt_adv = rstask.getString("availed_amt_adv");
                date_drawal_format = rstask.getString("date_drawal_format");
                principal_paid = rstask.getString("principal_paid");
                amount_standing = rstask.getString("amount_standing");
                officer_leave = rstask.getString("officer_leave");
                date_commencement_format = rstask.getString("date_commencement_format");
                date_expire_format = rstask.getString("date_expire_format");
                apply_to = rstask.getString("apply_to");
                pending_spc = rstask.getString("pending_spc");
                note = rstask.getString("note");
                if (rstask.getString("disk_file_name") != null && !rstask.getString("disk_file_name").equals("")) {
                    disk_file_name = rstask.getString("original_filename");
                }
                comments = rstask.getString("comments");
                loan_type = rstask.getString("loan_type");

            }
            String apply_name = "";
            String apply_spn = "";
            String apply_emp = "";
            psapply = con.prepareStatement("SELECT ARRAY_TO_STRING(ARRAY[INITIALS, a.F_NAME, a.M_NAME,a.L_NAME], ' ') fullname,a.cur_basic_salary,to_char(a.dob, 'DD-Mon-YYYY') as dob,to_char(a.dos, 'DD-Mon-YYYY') as dos,b.spn,b.spc,c.off_name  FROM emp_mast a,g_spc b,g_office c WHERE a.cur_spc=b.spc AND a.cur_off_code=c.off_code AND   emp_id=?");
            psapply.setString(1, rstask.getString("apply_to"));
            rsapply = psapply.executeQuery();
            if (rsapply.next()) {
                apply_name = rsapply.getString("fullname");
                apply_spn = rsapply.getString("spn");
                apply_emp = apply_name + " (" + apply_spn + ")";

            }
            ps = con.prepareStatement("SELECT a.is_regular,a.emp_id,ARRAY_TO_STRING(ARRAY[INITIALS, a.F_NAME, a.M_NAME,a.L_NAME], ' ') fullname,a.cur_basic_salary,to_char(a.dob, 'DD-Mon-YYYY') as dob,to_char(a.dos, 'DD-Mon-YYYY') as dos,b.spn,b.spc,c.off_name  FROM emp_mast a,g_spc b,g_office c WHERE a.cur_spc=b.spc AND a.cur_off_code=c.off_code AND   emp_id=?");
            ps.setString(1, apply_hrmsid);
            rs = ps.executeQuery();

            String fullname = "";
            String cur_basic_salary = "";
            String spn = "";
            String spc = "";
            String off_name = "";
            String jobtype = "";
            String dob = "";
            String dos = "";
            String netsalary = "";
            if (rs.next()) {
                fullname = rs.getString("fullname");
                cur_basic_salary = rs.getString("cur_basic_salary");
                spn = rs.getString("spn");
                spc = rs.getString("spc");
                off_name = rs.getString("off_name");
                netsalary = getNetPay(apply_hrmsid, cmonth, cyear) + "";
                if (rs.getString("is_regular").equals("Y")) {
                    jobtype = "Permanent";
                } else {
                    jobtype = "Temporary";
                }
                dob = rs.getString("dob");
                dos = rs.getString("dos");

            }

            Font f1 = new Font();
            f1.setSize(10);
            f1.setFamily("Times New Roman");
            Font hdrTextFont = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD, BaseColor.BLACK);
            Font hdrTextFont1 = new Font(Font.FontFamily.HELVETICA, 6, Font.BOLD, BaseColor.BLACK);
            Font dataHdrFont = new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD, BaseColor.BLACK);
            Font dataHdrFont1 = new Font(Font.FontFamily.HELVETICA, 8, Font.UNDERLINE, BaseColor.BLACK);
            Font dataValFont = new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK);

            PdfPTable table = null;
            table = new PdfPTable(1);
            table.setWidths(new int[]{5});
            table.setWidthPercentage(100);
            PdfPCell cell = null;

            cell = new PdfPCell(new Phrase("Application form for Advance for the purpose of Motor car/motor cycle/Moped/personal computer", hdrTextFont));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Loan Id: PL" + loanid, dataHdrFont1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            document.add(table);

            PdfPTable table1 = new PdfPTable(2);
            table1.setWidths(new int[]{3, 5});
            table1.setWidthPercentage(100);
            PdfPCell datacell;

            datacell = new PdfPCell(new Paragraph(" ", dataHdrFont));
            datacell.setBorder(Rectangle.NO_BORDER);
            datacell.setColspan(2);
            datacell.setFixedHeight(15);
            datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("1. Name:", hdrTextFont1));
            datacell.setBorder(Rectangle.NO_BORDER);
            datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase(fullname, hdrTextFont1));
            datacell.setBorder(Rectangle.NO_BORDER);
            datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("2. Designation:", hdrTextFont1));
            datacell.setBorder(Rectangle.NO_BORDER);
            datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase(spn, hdrTextFont1));
            datacell.setBorder(Rectangle.NO_BORDER);
            datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("3. Official Address:", hdrTextFont1));
            datacell.setBorder(Rectangle.NO_BORDER);
            datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase(off_name, hdrTextFont1));
            datacell.setBorder(Rectangle.NO_BORDER);
            datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("4. Job Type:", hdrTextFont1));
            datacell.setBorder(Rectangle.NO_BORDER);
            datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase(jobtype, hdrTextFont1));
            datacell.setBorder(Rectangle.NO_BORDER);
            datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("5. Basic  salary:", hdrTextFont1));
            datacell.setBorder(Rectangle.NO_BORDER);
            datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase(cur_basic_salary, hdrTextFont1));
            datacell.setBorder(Rectangle.NO_BORDER);
            datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("6. Net  salary:", hdrTextFont1));
            datacell.setBorder(Rectangle.NO_BORDER);
            datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase(netsalary, hdrTextFont1));
            datacell.setBorder(Rectangle.NO_BORDER);
            datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("7. DOB:", hdrTextFont1));
            datacell.setBorder(Rectangle.NO_BORDER);
            datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase(dob, hdrTextFont1));
            datacell.setBorder(Rectangle.NO_BORDER);
            datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("8. Date of Superannuation:", hdrTextFont1));
            datacell.setBorder(Rectangle.NO_BORDER);
            datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase(dos, hdrTextFont1));
            datacell.setBorder(Rectangle.NO_BORDER);
            datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("9. Loan Apply For:", hdrTextFont1));
            datacell.setBorder(Rectangle.NO_BORDER);
            datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase(loan_type, hdrTextFont1));
            datacell.setBorder(Rectangle.NO_BORDER);
            datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("10. Anticipated Price:", hdrTextFont1));
            datacell.setBorder(Rectangle.NO_BORDER);
            datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase(ant_price, hdrTextFont1));
            datacell.setBorder(Rectangle.NO_BORDER);
            datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("11. Purchase Type:", hdrTextFont1));
            datacell.setBorder(Rectangle.NO_BORDER);
            datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase(purchase_type, hdrTextFont1));
            datacell.setBorder(Rectangle.NO_BORDER);
            datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("12. Amount of advance required:", hdrTextFont1));
            datacell.setBorder(Rectangle.NO_BORDER);
            datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase(amount_adv, hdrTextFont1));
            datacell.setBorder(Rectangle.NO_BORDER);
            datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("13. No of instalments:", hdrTextFont1));
            datacell.setBorder(Rectangle.NO_BORDER);
            datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase(no_installment + "", hdrTextFont1));
            datacell.setBorder(Rectangle.NO_BORDER);
            datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("14. Whether advance for similar purpose was availed previously?", hdrTextFont1));
            datacell.setBorder(Rectangle.NO_BORDER);
            datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase(loan_availed, hdrTextFont1));
            datacell.setBorder(Rectangle.NO_BORDER);
            datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("15. Whether for Motor Car/Cycle/Moped", hdrTextFont1));
            datacell.setBorder(Rectangle.NO_BORDER);
            datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase(motorcar_cycle_moped, hdrTextFont1));
            datacell.setBorder(Rectangle.NO_BORDER);
            datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("16. Amount of  advance", hdrTextFont1));
            datacell.setBorder(Rectangle.NO_BORDER);
            datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase(availed_amt_adv, hdrTextFont1));
            datacell.setBorder(Rectangle.NO_BORDER);
            datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("17. Date of drawal advance", hdrTextFont1));
            datacell.setBorder(Rectangle.NO_BORDER);
            datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase(date_drawal_format, hdrTextFont1));
            datacell.setBorder(Rectangle.NO_BORDER);
            datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("18. Principal along with Interest paid in Full?", hdrTextFont1));
            datacell.setBorder(Rectangle.NO_BORDER);
            datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase(principal_paid, hdrTextFont1));
            datacell.setBorder(Rectangle.NO_BORDER);
            datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("19. Amount of principal/interest standing", hdrTextFont1));
            datacell.setBorder(Rectangle.NO_BORDER);
            datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase(amount_standing, hdrTextFont1));
            datacell.setBorder(Rectangle.NO_BORDER);
            datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("20. Wheather the officer is on leave or is about to proceed?", hdrTextFont1));
            datacell.setBorder(Rectangle.NO_BORDER);
            datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase(officer_leave, hdrTextFont1));
            datacell.setBorder(Rectangle.NO_BORDER);
            datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("21. Date of commencement leave", hdrTextFont1));
            datacell.setBorder(Rectangle.NO_BORDER);
            datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase(date_commencement_format, hdrTextFont1));
            datacell.setBorder(Rectangle.NO_BORDER);
            datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("22. Date of expire leave", hdrTextFont1));
            datacell.setBorder(Rectangle.NO_BORDER);
            datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase(date_expire_format, hdrTextFont1));
            datacell.setBorder(Rectangle.NO_BORDER);
            datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("23. Attachment:", hdrTextFont1));
            datacell.setBorder(Rectangle.NO_BORDER);
            datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase(disk_file_name, hdrTextFont1));
            datacell.setBorder(Rectangle.NO_BORDER);
            datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(datacell);
            if (!apply_emp.equals("")) {
                datacell = new PdfPCell(new Phrase("24. Forwarded to:", hdrTextFont1));
                datacell.setBorder(Rectangle.NO_BORDER);
                datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table1.addCell(datacell);

                datacell = new PdfPCell(new Phrase(apply_emp, hdrTextFont1));
                datacell.setBorder(Rectangle.NO_BORDER);
                datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table1.addCell(datacell);
            }

            datacell = new PdfPCell(new Phrase("25.Notes:", hdrTextFont1));
            datacell.setBorder(Rectangle.NO_BORDER);
            datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase(note, hdrTextFont1));
            datacell.setBorder(Rectangle.NO_BORDER);
            datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(datacell);

            document.add(table1);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }

    }

    @Override
    public void downloadLoanTypeGPFRefund(Document document, int loanid, String hrmsid) {
        LoanGPFForm loanvalue = new LoanGPFForm();
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement pstask = null;
        PreparedStatement psapply = null;
        ResultSet rs = null;
        ResultSet rstask = null;
        ResultSet rsapply = null;
        Calendar now = Calendar.getInstance();
        int cmonth = now.get(Calendar.MONTH);
        int cyear = now.get(Calendar.YEAR);
        int previousYear = cyear;
        String empId = "";
        String closing_balance = "";
        String balancecredit = "";
        String credit_from = "";
        String credit_to = "";
        String credit_amount = "";
        String refund_amount = "";
        String withdrawal_from = "";
        String withdrawal_to = "";
        String withdrawal_amount = "";
        String net_balance = "";
        String amount_required = "";
        String purpose = "";
        String rule_gpf = "";
        String same_purpse_details = "";
        String apply_to = "";
        String comments = "";
        String disk_file_name = "";
        String gpftypestatus = null;

        try {
            con = this.dataSource.getConnection();

            con = this.dataSource.getConnection();
            pstask = con.prepareStatement("SELECT a.*,b.* FROM hw_emp_gpf_loan a,task_master b WHERE a.taskid=b.task_id AND a.loan_gpf_id=? ");
            pstask.setInt(1, loanid);
            rstask = pstask.executeQuery();
            if (rstask.next()) {
                empId = rstask.getString("emp_id");
                closing_balance = rstask.getString("closing_balance");
                balancecredit = rstask.getString("balancecredit");
                credit_from = rstask.getString("credit_from");
                credit_to = rstask.getString("credit_to");
                credit_amount = rstask.getString("credit_amount");
                refund_amount = rstask.getString("refund_amount");
                withdrawal_from = rstask.getString("withdrawal_from");
                withdrawal_to = rstask.getString("withdrawal_to");
                withdrawal_amount = rstask.getString("withdrawal_amount");
                net_balance = rstask.getString("net_balance");
                amount_required = rstask.getString("amount_required");
                purpose = rstask.getString("purpose");
                rule_gpf = rstask.getString("rule_gpf");
                same_purpse_details = rstask.getString("same_purpse_details");
                apply_to = rstask.getString("apply_to");
                comments = rstask.getString("comments");
                gpftypestatus = rstask.getString("gpftype");
                gpftypestatus = gpftypestatus.trim();
                if (rstask.getString("disk_file_name") != null && !rstask.getString("disk_file_name").equals("")) {
                    disk_file_name = rstask.getString("original_filename");
                }

            }
            String apply_name = "";
            String apply_spn = "";
            String apply_emp = "";
            psapply = con.prepareStatement("SELECT ARRAY_TO_STRING(ARRAY[INITIALS, a.F_NAME, a.M_NAME,a.L_NAME], ' ') fullname,a.cur_basic_salary,to_char(a.dob, 'DD-Mon-YYYY') as dob,to_char(a.dos, 'DD-Mon-YYYY') as dos,b.spn,b.spc,c.off_name  FROM emp_mast a,g_spc b,g_office c WHERE a.cur_spc=b.spc AND a.cur_off_code=c.off_code AND   emp_id=?");
            psapply.setString(1, rstask.getString("apply_to"));
            rsapply = psapply.executeQuery();
            if (rsapply.next()) {
                apply_name = rsapply.getString("fullname");
                apply_spn = rsapply.getString("spn");
                apply_emp = apply_name + " (" + apply_spn + ")";

            }
            String fullname = "";
            String cur_basic_salary = "";
            String accountNo = "";
            String spc = "";
            String off_name = "";
            String dos = "";
            String basicsalary_gp_data = "";
            String doj = "";
            String netsalary = "";
            String post = "";
            int remainingAge = 0;

            String ddooffice = "";
            int serviceYear = 0;

            ps = con.prepareStatement("SELECT a.gpf_no,a.gp,a.emp_id,ARRAY_TO_STRING(ARRAY[INITIALS, a.F_NAME, a.M_NAME,"
                    + "a.L_NAME], ' ') fullname,a.cur_basic_salary,to_char(a.JOINDATE_OF_GOO, 'DD-Mon-YYYY') as doj,"
                    + "to_char(a.dos, 'DD-Mon-YYYY') as dos,b.spn,b.spc,b.gpc,c.post,d.department_name,"
                    + "EXTRACT(YEAR from AGE(NOW(), a.JOINDATE_OF_GOO)) as age,EXTRACT(YEAR from AGE(a.dos,NOW())) as remainingage,e.ddo_code,e.off_en  FROM emp_mast a,g_spc b,g_post c,"
                    + "g_department d,g_office e WHERE  a.cur_spc=b.spc AND b.dept_code=d.department_code AND  "
                    + " b.gpc=c.post_code AND a.cur_off_code=e.off_code AND emp_id=?");
            ps.setString(1, empId);
            rs = ps.executeQuery();
            if (rs.next()) {
                fullname = rs.getString("fullname");
                String gpfno = rs.getString("gpf_no");
                spc = rs.getString("spc");
                String deptname = rs.getString("department_name");
                accountNo = gpfno + " (" + deptname + ")";
                serviceYear = rs.getInt("age");
                remainingAge = rs.getInt("remainingage");

                String basicsalary_gp = rs.getString("cur_basic_salary") + "+ GP " + rs.getString("gp");
                post = rs.getString("post");
                basicsalary_gp_data = basicsalary_gp;
                doj = rs.getString("doj");
                dos = rs.getString("dos");

                if (cmonth < 4) {
                    previousYear = cyear - 1;
                    cyear = previousYear;

                }
                if (cmonth == 0) {
                    cmonth = 12;
                }

                String ddo_code = rs.getString("ddo_code");
                String ddo_office = rs.getString("off_en");
                ddooffice = ddo_office + "(" + ddo_code + ")";

                loanvalue.setDdocode(ddo_code);

            }

            Font f1 = new Font();
            f1.setSize(10);
            f1.setFamily("Times New Roman");
            Font hdrTextFont = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD, BaseColor.BLACK);
            Font hdrTextFont1 = new Font(Font.FontFamily.HELVETICA, 6, Font.BOLD, BaseColor.BLACK);
            Font dataHdrFont = new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD, BaseColor.BLACK);
            Font dataValFont = new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK);

            PdfPTable table = null;
            table = new PdfPTable(1);
            table.setWidths(new int[]{5});
            table.setWidthPercentage(100);
            PdfPCell cell = null;

            cell = new PdfPCell(new Phrase("PROFORMA FOR APPLICATION OF " + gpftypestatus + " WITHDRAWAL FROM GPF", hdrTextFont));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            document.add(table);

            PdfPTable table1 = new PdfPTable(2);
            table1.setWidths(new int[]{3, 5});
            table1.setWidthPercentage(100);
            PdfPCell datacell;

            datacell = new PdfPCell(new Paragraph(" ", dataHdrFont));
            datacell.setBorder(Rectangle.NO_BORDER);
            datacell.setColspan(2);
            datacell.setFixedHeight(15);
            datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("1. Name of the subscriber:", hdrTextFont1));
            datacell.setBorder(Rectangle.NO_BORDER);
            datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase(fullname, hdrTextFont1));
            datacell.setBorder(Rectangle.NO_BORDER);
            datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("2. Account Number:", hdrTextFont1));
            datacell.setBorder(Rectangle.NO_BORDER);
            datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase(accountNo, hdrTextFont1));
            datacell.setBorder(Rectangle.NO_BORDER);
            datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("3. Post:", hdrTextFont1));
            datacell.setBorder(Rectangle.NO_BORDER);
            datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase(post, hdrTextFont1));
            datacell.setBorder(Rectangle.NO_BORDER);
            datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("4. Pay:", hdrTextFont1));
            datacell.setBorder(Rectangle.NO_BORDER);
            datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase(basicsalary_gp_data, hdrTextFont1));
            datacell.setBorder(Rectangle.NO_BORDER);
            datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("5(i). Date of Joining Service:", hdrTextFont1));
            datacell.setBorder(Rectangle.NO_BORDER);
            datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase(doj, hdrTextFont1));
            datacell.setBorder(Rectangle.NO_BORDER);
            datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("5(ii). Date of Superannuation:", hdrTextFont1));
            datacell.setBorder(Rectangle.NO_BORDER);
            datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase(dos, hdrTextFont1));
            datacell.setBorder(Rectangle.NO_BORDER);
            datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("6. GPF Type:", hdrTextFont1));
            datacell.setBorder(Rectangle.NO_BORDER);
            datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase(gpftypestatus, hdrTextFont1));
            datacell.setBorder(Rectangle.NO_BORDER);
            datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("7. Balance at the credit of the subscriber on the date of application :", hdrTextFont1));
            datacell.setBorder(Rectangle.NO_BORDER);
            datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase(balancecredit + "/-", hdrTextFont1));
            datacell.setBorder(Rectangle.NO_BORDER);
            datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("7(i). Closing balance as per statement for the Year " + cyear + ":", hdrTextFont1));
            datacell.setBorder(Rectangle.NO_BORDER);
            datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase(closing_balance + "/-", hdrTextFont1));
            datacell.setBorder(Rectangle.NO_BORDER);
            datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("7(ii). Credit from " + credit_from + " to " + credit_to + " :", hdrTextFont1));
            datacell.setBorder(Rectangle.NO_BORDER);
            datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase(credit_amount + "/-", hdrTextFont1));
            datacell.setBorder(Rectangle.NO_BORDER);
            datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("7(iii). Refunds made to the fund after the closing balance:", hdrTextFont1));
            datacell.setBorder(Rectangle.NO_BORDER);
            datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase(refund_amount + "/-", hdrTextFont1));
            datacell.setBorder(Rectangle.NO_BORDER);
            datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("7(iv). Withdrawal during the period from " + withdrawal_from + " to " + withdrawal_to + " :", hdrTextFont1));
            datacell.setBorder(Rectangle.NO_BORDER);
            datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase(withdrawal_amount + "/-", hdrTextFont1));
            datacell.setBorder(Rectangle.NO_BORDER);
            datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("7(v). Net Balance at credit on date of application:", hdrTextFont1));
            datacell.setBorder(Rectangle.NO_BORDER);
            datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase(net_balance + "/-", hdrTextFont1));
            datacell.setBorder(Rectangle.NO_BORDER);
            datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("8. Amount of withdrawal required:", hdrTextFont1));
            datacell.setBorder(Rectangle.NO_BORDER);
            datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase(amount_required + "/-", hdrTextFont1));
            datacell.setBorder(Rectangle.NO_BORDER);
            datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("9(a). Purpose for which the withdrawal is required", hdrTextFont1));
            datacell.setBorder(Rectangle.NO_BORDER);
            datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase(purpose, hdrTextFont1));
            datacell.setBorder(Rectangle.NO_BORDER);
            datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("9(b). Rule under which the request is covered ", hdrTextFont1));
            datacell.setBorder(Rectangle.NO_BORDER);
            datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase(rule_gpf, hdrTextFont1));
            datacell.setBorder(Rectangle.NO_BORDER);
            datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("10. Whether any withdrawal was taken for the same purpose earlier if so, indicate the amount and the year", hdrTextFont1));
            datacell.setBorder(Rectangle.NO_BORDER);
            datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase(same_purpse_details, hdrTextFont1));
            datacell.setBorder(Rectangle.NO_BORDER);
            datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("11. Attachment:", hdrTextFont1));
            datacell.setBorder(Rectangle.NO_BORDER);
            datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase(disk_file_name, hdrTextFont1));
            datacell.setBorder(Rectangle.NO_BORDER);
            datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("12. Name of the account officer maintaining the provident Fund account:", hdrTextFont1));
            datacell.setBorder(Rectangle.NO_BORDER);
            datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase(ddooffice, hdrTextFont1));
            datacell.setBorder(Rectangle.NO_BORDER);
            datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(datacell);

            if (!apply_emp.equals("")) {
                datacell = new PdfPCell(new Phrase("13. Forwarded to:", hdrTextFont1));
                datacell.setBorder(Rectangle.NO_BORDER);
                datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table1.addCell(datacell);

                datacell = new PdfPCell(new Phrase(apply_emp, hdrTextFont1));
                datacell.setBorder(Rectangle.NO_BORDER);
                datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table1.addCell(datacell);
            }

            datacell = new PdfPCell(new Phrase("14.Notes:", hdrTextFont1));
            datacell.setBorder(Rectangle.NO_BORDER);
            datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase(comments, hdrTextFont1));
            datacell.setBorder(Rectangle.NO_BORDER);
            datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(datacell);
            document.add(table1);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
            document.close();
        }

    }

    @Override
    public void gpftemporarylPDF(Document document, String filePath, int loanid) {
        Connection con = null;
        LoanGPFForm loanvalue = new LoanGPFForm();
        PreparedStatement ps = null;
        ResultSet rs = null;

        String empId = null;
        try {

            String letterno = "";
            String letterdate = "";
            String letterfrom = "";
            String fromdesignation = "";

            String memono = "";
            String memodate = "";
            String san_date = "";
            String san_no = "";

            String issue_no = "";
            String letterto = "";
            String file_no = "";
            String closing_balance = "";
            String balancecredit = "";
            String credit_from = "";
            String credit_to = "";

            String credit_amount = "";
            String refund_amount = "";
            String withdrawal_from = "";
            String withdrawal_to = "";

            String withdrawal_amount = "";
            String net_balance = "";
            String amount_required = "";
            String purpose = "";
            String gpftype = "";
            String rule_gpf = "";
            String same_purpse_details = "";

            Calendar now = Calendar.getInstance();
            int cmonth = now.get(Calendar.MONTH);
            int cyear = now.get(Calendar.YEAR);
            int previousYear = cyear;

            con = this.dataSource.getConnection();
            ps = con.prepareStatement("SELECT a.* FROM hw_emp_gpf_loan a WHERE loan_gpf_id=?");
            ps.setInt(1, loanid);
            rs = ps.executeQuery();
            if (rs.next()) {
                letterno = rs.getString("letterno");
                letterdate = rs.getString("letterdate");
                letterfrom = rs.getString("letterfrom");
                fromdesignation = rs.getString("fromdesignation");
                letterto = rs.getString("letterto");
                memono = rs.getString("memono");
                memodate = rs.getString("memodate");
                san_date = rs.getString("san_date");
                san_no = rs.getString("san_no");
                issue_no = rs.getString("issue_no");
                file_no = rs.getString("file_no");

                closing_balance = rs.getString("closing_balance");
                balancecredit = rs.getString("balancecredit");
                credit_from = rs.getString("credit_from");
                credit_to = rs.getString("credit_to");
                credit_amount = rs.getString("credit_amount");
                refund_amount = rs.getString("refund_amount");
                withdrawal_from = rs.getString("withdrawal_from");
                withdrawal_to = rs.getString("withdrawal_to");
                withdrawal_amount = rs.getString("withdrawal_amount");
                net_balance = rs.getString("net_balance");
                amount_required = rs.getString("amount_required");
                purpose = rs.getString("purpose");
                rule_gpf = rs.getString("rule_gpf");
                same_purpse_details = rs.getString("same_purpse_details");
                gpftype = rs.getString("gpftype");

                if (cmonth <= 3) {
                    previousYear = cyear - 1;
                    //cyear = previousYear;

                }
                if (cmonth == 0) {
                    cmonth = 12;
                }

            }
            String Loanee_name = "";
            String Loanee_des = "";
            String basic_gp = "";
            String gpf_no = "";
            String department_name = "";

            ps = con.prepareStatement("SELECT a.is_regular,a.gpf_no,a.gp,a.emp_id,ARRAY_TO_STRING(ARRAY[INITIALS, a.F_NAME, a.M_NAME,"
                    + "a.L_NAME], ' ') fullname,to_char(a.dob, 'DD-Mon-YYYY') as dob,a.cur_basic_salary,to_char(a.JOINDATE_OF_GOO, 'DD-Mon-YYYY') as doj,"
                    + "to_char(a.dos, 'DD-Mon-YYYY') as dos,b.spn,b.spc,b.gpc,c.post,d.department_name,"
                    + "EXTRACT(YEAR from AGE(NOW(), a.JOINDATE_OF_GOO)) as age,EXTRACT(YEAR from AGE(a.dos,NOW())) as remainingage,e.ddo_code,e.off_en  FROM emp_mast a,g_spc b,g_post c,"
                    + "g_department d,g_office e WHERE  a.cur_spc=b.spc AND b.dept_code=d.department_code AND  "
                    + " b.gpc=c.post_code AND a.cur_off_code=e.off_code AND emp_id=?");

            ps.setString(1, empId);
            rs = ps.executeQuery();
            if (rs.next()) {
                Loanee_name = rs.getString("fullname");
                Loanee_des = rs.getString("post");

                String loanee_basicsalary = rs.getString("cur_basic_salary");
                String loanee_gp = rs.getString("gp");
                basic_gp = "RS " + loanee_basicsalary + "/-" + " " + "+" + "" + "RS " + loanee_gp + "/-" + " (G.P)";
                gpf_no = rs.getString("gpf_no");
                department_name = rs.getString("department_name");
            }
            int totalBalnce = Integer.parseInt(credit_amount) + Integer.parseInt(closing_balance);
            int netblance = totalBalnce - Integer.parseInt(withdrawal_amount);

            document.open();

            Font f1 = new Font();
            f1.setSize(10);
            f1.setFamily("Times New Roman");

            PdfPTable table = null;
            PdfPCell cell = null;

            table = new PdfPTable(1);
            //table.setWidths(new int[]{2, 3});
            table.setWidthPercentage(100);

            cell = new PdfPCell(new Phrase("Schedule L III -- Form No. 239", f1));
            cell.setFixedHeight(20);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(" G.P.F. A/C No. " + gpf_no, new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.UNDERLINE)));
            cell.setFixedHeight(20);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("GOVERNMENT OF ODISHA", new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD)));
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(department_name, new Font(Font.FontFamily.TIMES_ROMAN, 12)));
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Sanction No. " + san_no + " / Sanction Date: " + san_date, new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.UNDERLINE)));
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("OFFICE ORDER", new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.UNDERLINE)));
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            Phrase phrs = new Phrase();
            Chunk c1 = new Chunk("File No. : ", f1);
            Chunk c2 = new Chunk(file_no, new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD));
            phrs.add(c1);
            phrs.add(c2);

            cell = new PdfPCell(phrs);
            cell.setColspan(4);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(20);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            phrs = new Phrase();
            c1 = new Chunk("Issue No.     ", f1);
            c2 = new Chunk(issue_no, new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD));
            phrs.add(c1);
            phrs.add(c2);
            cell = new PdfPCell(phrs);
            cell.setColspan(4);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(" 1. Sanction is hereby accorded under GPF Rule 15/16 of General Provident Fund (Odisha) Rule for grant of advance of\n"
                    + "Rs. " + withdrawal_amount + " to " + Loanee_name + "," + Loanee_des + " from his/her\n"
                    + "GPF/AISPF/AEIPF Account No." + gpf_no + " to enable him/her to meet the expenditure towards " + same_purpse_details + ".", f1));

            //cell.setFixedHeight(80);
            cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("2. The Sanctioned amount is debitable to the Head of account 00-8009-STATEPROVIDENTFUNDS-01-101-\n"
                    + "GENERALPROVIDENTFUNDS-1686-EmployeesProvidentFund-91043-DepositsofStateGovt.Employees-000-Deposits of State Govt. Employees.", f1));

            //cell.setFixedHeight(80);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("3. The balance at the credit of " + Loanee_name + "," + Loanee_des + " is detailed below;", f1));

            cell.setFixedHeight(80);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            document.add(table);

            table = new PdfPTable(4);
            table.setWidths(new int[]{3, 2, 2, 2});
            table.setWidthPercentage(100);

            cell = new PdfPCell(new Phrase("1. Balance as per Account Slip for ", f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(cyear + "", f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Rs. ", f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(closing_balance, f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("2. Subsequent deposit from ", f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(credit_from + " to " + credit_to, f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Rs. ", f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(credit_amount, f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("3. Total of Column 1 & 2 above", f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("________ ", f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Rs. ", f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(totalBalnce + "", f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("4. Subsequent withdrawal if any", f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("________ ", f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Rs. ", f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(withdrawal_amount, f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("5.Net Balance", f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("________ ", f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(" ", f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(netblance + "", f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            document.add(table);

            table = new PdfPTable(1);
            // table.setWidths(new int[]{2, 3, 3, 3});
            table.setWidthPercentage(100);

            cell = new PdfPCell(new Phrase(" Certified that, the annual statment of recovery of the G.P.F of individual subscriber has been sent to the A.G(A &\n"
                    + "E),Odisha,Bhubaneswar vide letter No." + letterno + " dt." + letterdate + ".", f1));

            cell.setFixedHeight(80);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Specific Terms and Conditions:", new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.UNDERLINE)));
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("No specific terms & conditions specified:", new Font(Font.FontFamily.TIMES_ROMAN, 12)));
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("DEPUTY SECRETARY TO GOVERNMENT", new Font(Font.FontFamily.TIMES_ROMAN, 12)));
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            phrs = new Phrase();
            c1 = new Chunk("Memo Number ", f1);
            c2 = new Chunk(memono, new Font(Font.FontFamily.TIMES_ROMAN, 13, Font.BOLD));
            Chunk c3 = new Chunk(" Date ", f1);
            Chunk c4 = new Chunk(memodate, f1);
            phrs.add(c1);
            phrs.add(c2);
            phrs.add(c3);
            phrs.add(c4);

            cell = new PdfPCell(phrs);
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(" Copy forwarded to PRINCIPAL AG(A&E),ODISHA, BHUBANESWAR for information and necessary action.", f1));
            cell.setFixedHeight(80);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("DEPUTY SECRETARY TO GOVERNMENT", new Font(Font.FontFamily.TIMES_ROMAN, 12)));
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(StringUtils.repeat("_", 93), f1));
            cell.setColspan(4);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            document.add(table);

            table = new PdfPTable(3);
            table.setWidths(new int[]{2, 2, 2});
            table.setWidthPercentage(100);

            cell = new PdfPCell(new Phrase("Generated By: " + StringUtils.defaultString("________"), f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(" Date " + StringUtils.defaultString("________"), f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Page 1 of 2", f1));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setBorderWidth(1f);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);

            document.add(table);
            document.newPage();
            //Second Page

            table = new PdfPTable(1);
            // table.setWidths(new int[]{2, 3, 3, 3});
            table.setWidthPercentage(100);

            phrs = new Phrase();
            c1 = new Chunk("Memo Number ", f1);
            c2 = new Chunk("__________", new Font(Font.FontFamily.TIMES_ROMAN, 13, Font.BOLD));
            c3 = new Chunk(" Date ", f1);
            c4 = new Chunk("__________ ", f1);
            phrs.add(c1);
            phrs.add(c2);
            phrs.add(c3);
            phrs.add(c4);

            cell = new PdfPCell(phrs);
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(" Copy forwarded to TREASURY OFFICER, SPECIAL TREASURY NO-II,OLA CAMPUS,BHUBANESWAR. for\n"
                    + "information and necessary action.", f1));
            cell.setFixedHeight(80);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("DEPUTY SECRETARY TO GOVERNMENT", new Font(Font.FontFamily.TIMES_ROMAN, 12)));
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            phrs = new Phrase();
            c1 = new Chunk("Memo Number ", f1);
            c2 = new Chunk("__________", new Font(Font.FontFamily.TIMES_ROMAN, 13, Font.BOLD));
            c3 = new Chunk(" Date ", f1);
            c4 = new Chunk("__________ ", f1);
            phrs.add(c1);
            phrs.add(c2);
            phrs.add(c3);
            phrs.add(c4);

            cell = new PdfPCell(phrs);
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(" Copy forwarded to ACCOUNT SECTION-III (IN DUPLICATE) for information and necessary action.", f1));
            cell.setFixedHeight(80);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("DEPUTY SECRETARY TO GOVERNMENT", new Font(Font.FontFamily.TIMES_ROMAN, 12)));
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            phrs = new Phrase();
            c1 = new Chunk("Memo Number ", f1);
            c2 = new Chunk("__________", new Font(Font.FontFamily.TIMES_ROMAN, 13, Font.BOLD));
            c3 = new Chunk(" Date ", f1);
            c4 = new Chunk("__________ ", f1);
            phrs.add(c1);
            phrs.add(c2);
            phrs.add(c3);
            phrs.add(c4);

            cell = new PdfPCell(phrs);
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(" Copy forwarded to SMT TRUPTI DAS,SR DIARIST,G.A &P.G DEPARTMENT for information and necessary\n"
                    + "information and necessary action.", f1));
            cell.setFixedHeight(80);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("DEPUTY SECRETARY TO GOVERNMENT", new Font(Font.FontFamily.TIMES_ROMAN, 12)));
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(StringUtils.repeat("_", 93), f1));
            cell.setColspan(4);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            document.add(table);

            table = new PdfPTable(3);
            table.setWidths(new int[]{2, 2, 2});
            table.setWidthPercentage(100);

            cell = new PdfPCell(new Phrase("Generated By: " + StringUtils.defaultString("________"), f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(" Date " + StringUtils.defaultString("________"), f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Page 2 of 2", f1));
            cell.setColspan(2);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            document.add(table);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
            document.close();
        }

    }

    @Override
    public void gpfpartfinalPDF(Document document, String filePath) {
        Connection con = null;
        try {

            document.open();

            Font f1 = new Font();
            f1.setSize(10);
            f1.setFamily("Times New Roman");

            PdfPTable table = null;
            PdfPCell cell = null;

            table = new PdfPTable(1);
            //table.setWidths(new int[]{2, 3});
            table.setWidthPercentage(100);

            cell = new PdfPCell(new Phrase("Schedule  -- Form No.:", f1));
            cell.setFixedHeight(20);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(" G.P.F. A/C No.", new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.UNDERLINE)));
            cell.setFixedHeight(20);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("GOVERNMENT OF ODISHA", new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD)));
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("General Administration and Public Grievance", new Font(Font.FontFamily.TIMES_ROMAN, 12)));
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Sanction No. / Sanction Date:", new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.UNDERLINE)));
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("OFFICE ORDER", new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.UNDERLINE)));
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            Phrase phrs = new Phrase();
            Chunk c1 = new Chunk("File No. : ", f1);
            Chunk c2 = new Chunk("____________", new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD));
            phrs.add(c1);
            phrs.add(c2);

            cell = new PdfPCell(phrs);
            cell.setColspan(4);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(20);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            phrs = new Phrase();
            c1 = new Chunk("Issue No.     ", f1);
            c2 = new Chunk("_____________", new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD));
            phrs.add(c1);
            phrs.add(c2);
            cell = new PdfPCell(phrs);
            cell.setColspan(4);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("To,", f1));
            cell.setFixedHeight(20);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("The Accountant General (A & E),Odisha,Bhubaneswar,", f1));
            cell.setFixedHeight(20);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(20);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            phrs = new Phrase();
            c1 = new Chunk("Sub:     ", f1);
            c2 = new Chunk("Sanction of Non-refundable G.P.F. withdrawal in favour of _____________", new Font(Font.FontFamily.TIMES_ROMAN, 10));
            phrs.add(c1);
            phrs.add(c2);
            cell = new PdfPCell(phrs);
            cell.setColspan(4);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Sir/Madam,", f1));
            cell.setFixedHeight(20);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(" I am directed to convey the sanction for grant of GPF-Part Final withdrawl of Rs.2,28,000/- ( Two Lakh Twenty Eight)\n"
                    + "Thousand Only.) under GPF rule ................................................................ in favour of Sri/Smt " + "_____+,\n"
                    + "HEAD DRIVER from his/her GPF/AISPF/AEIPF Account No. GAO 52318 to enable him/her to meet the expenditure,\n"
                    + "towards _______________", f1));
            //cell.setFixedHeight(80);
            cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(" The sanction amount is debitable to Head of Account 00-8009-STATEPROVIDENTFUNDS-01-101-\n"
                    + "GENERALPROVIDENTFUNDS-1686-EmployeesProvidentFund-91043-DepositsofStateGovt.Employees-000-Deposits,\n"
                    + "of State Govt. Employees.", f1));
            //cell.setFixedHeight(80);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("The sanctioned amount does not exceed 75% of the balance amount outstanding at his/her credit in the G.P.F Account.", f1));
            //cell.setFixedHeight(80);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("It is certified that Sri/Srimati " + ("______") + " has completed more than 10 years of Govt. service.", f1));
            //cell.setFixedHeight(80);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            document.add(table);

            table = new PdfPTable(4);
            table.setWidths(new int[]{3, 2, 2, 2});
            table.setWidthPercentage(100);

            cell = new PdfPCell(new Phrase("1. Balance as per Account Slip for ", f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("________ ", f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Rs. ", f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("_________ ", f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("2. Subsequent deposit from ", f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("________ ", f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Rs. ", f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("_________ ", f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("3. Total of Column 1 & 2 above", f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("________ ", f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Rs. ", f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("_________ ", f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("4. Subsequent withdrawal if any", f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("________ ", f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Rs. ", f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("_________ ", f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("5.Net Balance", f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("________ ", f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(" ", f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("_________ ", f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            document.add(table);

            table = new PdfPTable(1);
            // table.setWidths(new int[]{2, 3, 3, 3});
            table.setWidthPercentage(100);

            cell = new PdfPCell(new Phrase(" Certified that, the annual statment of recovery of the G.P.F of individual subscriber has been sent to the A.G(A &\n"
                    + "E),Odisha,Bhubaneswar vide letter No.___________ dt.__________.", f1));
            // cell.setFixedHeight(80);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Specific Terms and Conditions:", new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.UNDERLINE)));
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("No specific terms & conditions specified:", new Font(Font.FontFamily.TIMES_ROMAN, 12)));
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("DEPUTY SECRETARY TO GOVERNMENT", new Font(Font.FontFamily.TIMES_ROMAN, 12)));
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(StringUtils.repeat("_", 93), f1));
            cell.setColspan(4);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            document.add(table);

            table = new PdfPTable(3);
            table.setWidths(new int[]{2, 2, 2});
            table.setWidthPercentage(100);

            cell = new PdfPCell(new Phrase("Generated By: " + StringUtils.defaultString("________"), f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(" Date " + StringUtils.defaultString("________"), f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Page 1 of 2", f1));
            cell.setColspan(2);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            document.add(table);
            document.newPage();
            //Second Page

            table = new PdfPTable(1);
            // table.setWidths(new int[]{2, 3, 3, 3});
            table.setWidthPercentage(100);

            phrs = new Phrase();
            c1 = new Chunk("Memo Number ", f1);
            c2 = new Chunk("__________", new Font(Font.FontFamily.TIMES_ROMAN, 13, Font.BOLD));
            Chunk c3 = new Chunk(" Date ", f1);
            Chunk c4 = new Chunk("__________ ", f1);
            phrs.add(c1);
            phrs.add(c2);
            phrs.add(c3);
            phrs.add(c4);

            cell = new PdfPCell(phrs);
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(" Copy forwarded to TREASURY OFFICER, SPECIAL TREASURY NO-II,OLA CAMPUS,BHUBANESWAR. for\n"
                    + "information and necessary action.", f1));
            cell.setFixedHeight(80);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("DEPUTY SECRETARY TO GOVERNMENT", new Font(Font.FontFamily.TIMES_ROMAN, 12)));
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            phrs = new Phrase();
            c1 = new Chunk("Memo Number ", f1);
            c2 = new Chunk("__________", new Font(Font.FontFamily.TIMES_ROMAN, 13, Font.BOLD));
            c3 = new Chunk(" Date ", f1);
            c4 = new Chunk("__________ ", f1);
            phrs.add(c1);
            phrs.add(c2);
            phrs.add(c3);
            phrs.add(c4);

            cell = new PdfPCell(phrs);
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(" Copy forwarded to ACCOUNT SECTION-III (IN DUPLICATE) for information and necessary action.", f1));
            cell.setFixedHeight(80);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("DEPUTY SECRETARY TO GOVERNMENT", new Font(Font.FontFamily.TIMES_ROMAN, 12)));
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            phrs = new Phrase();
            c1 = new Chunk("Memo Number ", f1);
            c2 = new Chunk("__________", new Font(Font.FontFamily.TIMES_ROMAN, 13, Font.BOLD));
            c3 = new Chunk(" Date ", f1);
            c4 = new Chunk("__________ ", f1);
            phrs.add(c1);
            phrs.add(c2);
            phrs.add(c3);
            phrs.add(c4);

            cell = new PdfPCell(phrs);
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(" Copy forwarded to SRI PARIKHIT MEKAP,HEAD DRIVER,G.A &P.G DEPARTMENT for information and\n"
                    + "necessary action.", f1));
            cell.setFixedHeight(80);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("DEPUTY SECRETARY TO GOVERNMENT", new Font(Font.FontFamily.TIMES_ROMAN, 12)));
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(StringUtils.repeat("_", 93), f1));
            cell.setColspan(4);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            document.add(table);

            table = new PdfPTable(3);
            table.setWidths(new int[]{2, 2, 2});
            table.setWidthPercentage(100);

            cell = new PdfPCell(new Phrase("Generated By: " + StringUtils.defaultString("________"), f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(" Date " + StringUtils.defaultString("________"), f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Page 2 of 2", f1));
            cell.setColspan(2);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            document.add(table);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
            document.close();
        }

    }

    @Override
    public void ifmssanctionadvancePDF(Document document, String filePath) {
        Connection con = null;
        try {

            document.open();

            Font f1 = new Font();
            f1.setSize(10);
            f1.setFamily("Times New Roman");

            PdfPTable table = null;
            PdfPCell cell = null;

            table = new PdfPTable(1);
            //table.setWidths(new int[]{2, 3});
            table.setWidthPercentage(100);

            cell = new PdfPCell(new Phrase("GOVERNMENT OF ODISHA", new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD)));
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("General Administration and Public Grievance", new Font(Font.FontFamily.TIMES_ROMAN, 12)));
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Sanction No. / Sanction Date:", new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.UNDERLINE)));
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            Phrase phrs = new Phrase();
            Chunk c1 = new Chunk("File No. : ", f1);
            Chunk c2 = new Chunk("____________", new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD));
            phrs.add(c1);
            phrs.add(c2);

            cell = new PdfPCell(phrs);
            cell.setColspan(4);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(20);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            phrs = new Phrase();
            c1 = new Chunk("Issue No.     ", f1);
            c2 = new Chunk("_____________", new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD));
            phrs.add(c1);
            phrs.add(c2);
            cell = new PdfPCell(phrs);
            cell.setColspan(4);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("To,", f1));
            cell.setFixedHeight(20);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("The Principal Accountant General", f1));
            cell.setFixedHeight(20);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Odisha, Bhubaneswar.", f1));
            cell.setFixedHeight(20);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(20);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            phrs = new Phrase();
            c1 = new Chunk("Sub:     ", f1);
            cell = new PdfPCell(new Phrase("Particulars Of sanction", new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.UNDERLINE)));
            c2 = new Chunk("Grant of ___ of Sri/Smt________", new Font(Font.FontFamily.TIMES_ROMAN, 10));
            phrs.add(c1);
            phrs.add(c2);
            cell = new PdfPCell(phrs);
            cell.setColspan(4);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(" In accordance with the Rules to regulate the grant of “Advances to State Government Servants for Building, etc.,\n"
                    + "Houses vide Finance Department Resolution No.Codes-22/59/21246/F., dated the 22nd June 1959, Resolution,\n"
                    + "No.40704/F., dated the 30th July 1987 read with Office Memorandum No. 24711/F,. dated the 17th June 1995,\n"
                    + "No.43825/F., dated the 15th October 1998, No.8399/F., date the 6th March 1999 and No.4470/F., dated the 2nd,\n"
                    + "February 2010, sanction is hereby accorded to an advance of 1156000(Eleven Lakh Fifty Six Thousand Only.) to,\n"
                    + "Sri/Smt. Naik Judhisthir, Designation- ADDITIONAL SECRETARY TO GOVT., P.E DEPARTMENT now working under,\n"
                    + "the Department/Office of P.E Department for Construction of Building on existing Plot and subject to the terms and,\n"
                    + "conditions mentioned overleaf:", f1));
            cell.setFixedHeight(80);
            cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("The charge is debitable to Demand No.05--Demand Number for Finance Department-7610-\n"
                    + "LOANSTOGOVERNMENTSERVANTS,ETC.-00-201-HOUSEBUILDINGADVANCES-0825-LoansandAdvances-48016-,\n"
                    + "000-HouseBuildingAdvance(Normal)-11-EOM-1-Voted-0-None in the Budget Estimate during the Financial Year 2019-2020.:", f1));
            //cell.setFixedHeight(80);
            cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Particulars Of sanction", new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.UNDERLINE)));
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(20);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            phrs = new Phrase();
            c1 = new Chunk("Purpose of Advance:     ", f1);
            c2 = new Chunk("Construction of Building on existing Plot", new Font(Font.FontFamily.TIMES_ROMAN, 10));
            phrs.add(c1);
            phrs.add(c2);
            cell = new PdfPCell(phrs);
            cell.setColspan(4);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Payee Details", new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.UNDERLINE)));
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            document.add(table);

            table = new PdfPTable(2);
            table.setWidths(new int[]{2, 2});
            table.setWidthPercentage(100);

            cell = new PdfPCell(new Phrase("Name of the Govt. Servant " + StringUtils.defaultString("________"), f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("GPF Series and Account Number " + StringUtils.defaultString("________"), f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Designation " + StringUtils.defaultString("________"), f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Office Name " + StringUtils.defaultString("________"), f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Address " + StringUtils.defaultString("________"), f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Date of BIrth " + StringUtils.defaultString("________"), f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Date of " + StringUtils.defaultString("________"), f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Basic Pay " + StringUtils.defaultString("________"), f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Whether Temporary/Permanent " + StringUtils.defaultString("________"), f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Advance Amount " + StringUtils.defaultString("________"), f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Installment Number (Principal) " + StringUtils.defaultString("________"), f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Installment Amount (Principal) " + StringUtils.defaultString("________"), f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Last Installment Amount " + StringUtils.defaultString("________"), f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Installment Number (Interest) " + StringUtils.defaultString("________"), f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Installment Amount (Interest) " + StringUtils.defaultString("________"), f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Rate of Interest in % " + StringUtils.defaultString("________"), f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Interest Type " + StringUtils.defaultString("________"), f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Penal Rate of Interest in % " + StringUtils.defaultString("________"), f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Moratorium Period Required " + StringUtils.defaultString("________"), f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(StringUtils.repeat("_", 93), f1));
            cell.setColspan(4);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            document.add(table);

            table = new PdfPTable(3);
            table.setWidths(new int[]{2, 2, 2});
            table.setWidthPercentage(100);

            cell = new PdfPCell(new Phrase("Generated By: " + StringUtils.defaultString("________"), f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(" Date " + StringUtils.defaultString("________"), f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Page 1 of 4", f1));
            cell.setColspan(2);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            document.add(table);
            document.newPage();

            table = new PdfPTable(1);
            //table.setWidths(new int[]{2, 3});
            table.setWidthPercentage(100);

            cell = new PdfPCell(new Phrase("Moratorium Period(in months) " + StringUtils.defaultString("________"), f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Date from which recovery will " + StringUtils.defaultString("________"), f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Payable Treasury " + StringUtils.defaultString("________"), f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Payable DDO " + StringUtils.defaultString("________"), f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            document.add(table);

            table = new PdfPTable(1);
            //table.setWidths(new int[]{2, 3});
            table.setWidthPercentage(100);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Specific Terms and Conditions:", new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.UNDERLINE)));
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("No specific terms & conditions specified:", new Font(Font.FontFamily.TIMES_ROMAN, 12)));
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Signature", new Font(Font.FontFamily.TIMES_ROMAN, 12)));
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Terms & Conditions:", new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.UNDERLINE)));
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(" 1.Out of the sanctioned amount mentioned above Naik Judhisthir,ADDITIONAL SECRETARY TO GOVT., P.E,\n"
                    + "DEPARTMENT office of the P.E Department shall draw an amount in the first installment equivalent to 50% of the total\n"
                    + "advance of Rs.1156000/- (Eleven Lakh Fifty Six Thousand Only.) on her/his executing an agreement in Form No.III as,\n"
                    + "prescribed in Finance Department Resolution No.Codes-22/59-6251/F, date 04.03.1960 in favour of Governor of,\n"
                    + "Odisha for the re-payment of Advance. (according to Option).,\n"
                    + "Sri/Smt. Naik Judhisthir, Designation- ADDITIONAL SECRETARY TO GOVT., P.E DEPARTMENT now working under,\n"
                    + "the Department/Office of P.E Department for Construction of Building on existing Plot and subject to the terms and,\n"
                    + "conditions mentioned overleaf:", f1));
            //cell.setFixedHeight(80);
            cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(" 2. The advance shall carry a simple interest 11.5% per annum from the date of drawal of the Advance or Month,\n"
                    + "following NA months.", f1));
            //cell.setFixedHeight(80);
            cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(" 3. The entire advance shall be recovered in 44 consecutive monthly installments Rs.25688/- and the last one being,\n"
                    + "Rs.25728/- towards Principal and Interest shall be recovered in 11 subsequent consecutive monthly installments from\n"
                    + "the Pay/Leave Salary Bill of the Government Servant during her/his Service period. Any portion of the Principal or,\n"
                    + "Interest remaining outstanding shall be recovered from the General Provident Fund/Pension including Dearness,\n"
                    + "Relief/Commuted Value of Pension/DCRG and Other Pre & Post Retirement Dues payable to him/her.", f1));
            //cell.setFixedHeight(80);
            cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(" 4. The recovery of the advance shall be effected through the monthly Pay Bill/Leave Salary Bill of Sri/Smt. Naik,\n"
                    + "Judhisthir now working under office of the P.E Department commencing from the month following NA months/month\n"
                    + "from the date of drawal of the 1st installment of the House Building Advance or from the month following completion of,\n"
                    + "the Construction of the House. Failure to do so will render him liable to refund the entire amount of advance together,\n"
                    + "with interest due thereon in lump sum. The date of completion of the building must be reported to the undersigned without delay.", f1));
            // cell.setFixedHeight(80);
            cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(" 5. The recoveries of advance will not be held up or postponed except with the prior concurrence of the authority.", f1));
            // cell.setFixedHeight(80);
            cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(" 6. The House proposed to be constructed thereon shall remain as the property of the Government until the advance,\n"
                    + "together with interest accrued thereon is fully recovered.", f1));
            //cell.setFixedHeight(80);
            cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(" 7. This Order will remain valid for one month or till end of the financial year whichever is earlier", f1));
            cell.setFixedHeight(80);
            cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(StringUtils.repeat("_", 93), f1));
            cell.setColspan(4);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            document.add(table);

            table = new PdfPTable(3);
            table.setWidths(new int[]{2, 2, 2});
            table.setWidthPercentage(100);

            cell = new PdfPCell(new Phrase("Generated By: " + StringUtils.defaultString("________"), f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(" Date " + StringUtils.defaultString("________"), f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Page 2 of 4", f1));
            cell.setColspan(2);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);

            document.add(table);

            document.newPage();

            table = new PdfPTable(1);
            //table.setWidths(new int[]{2, 3});
            table.setWidthPercentage(100);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Signature", new Font(Font.FontFamily.TIMES_ROMAN, 12)));
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            phrs = new Phrase();
            c1 = new Chunk("Memo Number ", f1);
            c2 = new Chunk("__________", new Font(Font.FontFamily.TIMES_ROMAN, 13, Font.BOLD));
            Chunk c3 = new Chunk(" Date ", f1);
            Chunk c4 = new Chunk("__________ ", f1);
            phrs.add(c1);
            phrs.add(c2);
            phrs.add(c3);
            phrs.add(c4);

            cell = new PdfPCell(phrs);
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Copy forwarded to DEPUTY SECRETARY TO GOVT., O.E-I BRANCH OF G.A & P.G DEPARTMENT for\n"
                    + "information and necessary action. She is requested to place an allotment of Rs.11,56,000/- at the disposal of the\n"
                    + "Controlling Officer(C.O Code-PEN001 & DDO Code-OLSPEN001) of Sri Yudhisthir Nayak, OAS(SAG),\n"
                    + "Additional Secretary to Govt., P.E Department", f1));
            cell.setFixedHeight(80);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("F.A-cum-Special Secretary to Govt., G.A & P.G Department", new Font(Font.FontFamily.TIMES_ROMAN, 12)));
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            phrs = new Phrase();
            c1 = new Chunk("Memo Number ", f1);
            c2 = new Chunk("__________", new Font(Font.FontFamily.TIMES_ROMAN, 13, Font.BOLD));
            c3 = new Chunk(" Date ", f1);
            c4 = new Chunk("__________ ", f1);
            phrs.add(c1);
            phrs.add(c2);
            phrs.add(c3);
            phrs.add(c4);

            cell = new PdfPCell(phrs);
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Copy forwarded to BUDGET BRANCH OF G.A & P.G DEPARTMENT for information and necessary action.", new Font(Font.FontFamily.TIMES_ROMAN, 12)));
            cell.setFixedHeight(80);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("F.A-cum-Special Secretary to Govt., G.A & P.G Department", new Font(Font.FontFamily.TIMES_ROMAN, 12)));
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            phrs = new Phrase();
            c1 = new Chunk("Memo Number ", f1);
            c2 = new Chunk("__________", new Font(Font.FontFamily.TIMES_ROMAN, 13, Font.BOLD));
            c3 = new Chunk(" Date ", f1);
            c4 = new Chunk("__________ ", f1);
            phrs.add(c1);
            phrs.add(c2);
            phrs.add(c3);
            phrs.add(c4);

            cell = new PdfPCell(phrs);
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Copy forwarded to TREASURY OFFICER, SPECIAL TREASURY-II, OLA CAMPUS, BHUBANESWAR for\n"
                    + "information and necessary action. He is requested to honour the bill for drawal of the amount of the 1st\n"
                    + "instalment (50% of the advance) after release order has been issued by the Sanctioning Authority.", f1));
            cell.setFixedHeight(80);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("F.A-cum-Special Secretary to Govt., G.A & P.G Department", new Font(Font.FontFamily.TIMES_ROMAN, 12)));
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(StringUtils.repeat("_", 93), f1));
            cell.setColspan(4);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            document.add(table);

            table = new PdfPTable(3);
            table.setWidths(new int[]{2, 2, 2});
            table.setWidthPercentage(100);

            cell = new PdfPCell(new Phrase("Generated By: " + StringUtils.defaultString("________"), f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(" Date " + StringUtils.defaultString("________"), f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Page 3 of 4", f1));
            cell.setColspan(2);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);

            document.add(table);

            document.newPage();

            table = new PdfPTable(1);
            //table.setWidths(new int[]{2, 3});
            table.setWidthPercentage(100);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            phrs = new Phrase();
            c1 = new Chunk("Memo Number ", f1);
            c2 = new Chunk("__________", new Font(Font.FontFamily.TIMES_ROMAN, 13, Font.BOLD));
            c3 = new Chunk(" Date ", f1);
            c4 = new Chunk("__________ ", f1);
            phrs.add(c1);
            phrs.add(c2);
            phrs.add(c3);
            phrs.add(c4);

            cell = new PdfPCell(phrs);
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Copy forwarded to FINANCE DEPARTMENT for information and necessary action.", new Font(Font.FontFamily.TIMES_ROMAN, 12)));
            cell.setFixedHeight(80);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("F.A-cum-Special Secretary to Govt., G.A & P.G Department", new Font(Font.FontFamily.TIMES_ROMAN, 12)));
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            document.add(table);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
            document.close();
        }
    }

    @Override
    public void saveGpfLoansaction(LoanGPFForm lform) {
        Connection con = null;
        Calendar now = Calendar.getInstance();
        Date loanapplyDate = now.getTime();

        PreparedStatement ps = null;
        ResultSet rs = null;
        Date dt1 = null;
        String pending_at = null;
        String pending_spc = null;
        int taskid = lform.getTaskid();
        int loanid = lform.getLoanId();

        String sanno = lform.getSanNo();
        String sandate = lform.getSanDate();
        String fileno = lform.getFileno();
        String issueNo = lform.getIssueNo();
        String memoNo = lform.getMemoNo();
        String memoDate = lform.getMemoDate();
        String letterno = lform.getLetterNo();
        String letterdate = lform.getLetterDate();

        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("UPDATE hw_emp_gpf_loan SET memono=?,memodate=?,san_no=?,san_date=?,file_no=?,issue_no=?,letterno=?,letterdate=? WHERE taskid=? AND loan_gpf_id=?");
            ps.setString(1, memoNo);
            ps.setString(2, memoDate);
            ps.setString(3, sanno);
            ps.setString(4, sandate);
            ps.setString(5, fileno);
            ps.setString(6, issueNo);
            ps.setString(7, letterno);
            ps.setString(8, letterdate);
            ps.setInt(9, taskid);
            ps.setInt(10, loanid);
            int stsTask = ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void downloadHBALoan(Document document, int loanid, String hrmsid) {
        LoanForm loanvalue = new LoanForm();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        // PreparedStatement rstask = null;
        PreparedStatement psapply = null;
        PreparedStatement pstask = null;
        ResultSet rsapply = null;
        ResultSet rstask = null;

        Calendar now = Calendar.getInstance();
        int cmonth = now.get(Calendar.MONTH);
        int cyear = now.get(Calendar.YEAR);

        try {
            con = this.dataSource.getConnection();
            pstask = con.prepareStatement("SELECT a.*,b.* FROM hw_emp_hba_loan a,task_master b WHERE a.taskid=b.task_id AND loan_hba_id=?");
            pstask.setInt(1, loanid);
            rstask = pstask.executeQuery();

            String comments = null;
            String jobtype = "";
            int no_installment = 0;
            String purchase_type = "";
            String amount_adv = "";
            int instalments = 0;
            String loan_availed = "";
            String motorcar_cycle_moped = "";
            String availed_amt_adv = "";
            String date_drawal_format = "";
            String principal_paid = "";
            String amount_standing = "";
            String officer_leave = "";
            String date_commencement_format = "";

            String date_expire_format = "";
            String apply_to = "";
            String pending_spc = "";
            String disk_file_name = "";
            String apply_hrmsid = null;
            String loan_type = "";
            String note = "";

            if (rstask.next()) {
                apply_hrmsid = rstask.getString("emp_id");
            }
            String apply_name = "";
            String apply_spn = "";
            String apply_emp = "";
            psapply = con.prepareStatement("SELECT ARRAY_TO_STRING(ARRAY[INITIALS, a.F_NAME, a.M_NAME,a.L_NAME], ' ') fullname,a.cur_basic_salary,to_char(a.dob, 'DD-Mon-YYYY') as dob,to_char(a.dos, 'DD-Mon-YYYY') as dos,b.spn,b.spc,c.off_name  FROM emp_mast a,g_spc b,g_office c WHERE a.cur_spc=b.spc AND a.cur_off_code=c.off_code AND   emp_id=?");
            psapply.setString(1, rstask.getString("apply_to"));
            rsapply = psapply.executeQuery();
            if (rsapply.next()) {
                apply_name = rsapply.getString("fullname");
                apply_spn = rsapply.getString("spn");
                apply_emp = apply_name + " (" + apply_spn + ")";

            }
            ps = con.prepareStatement("SELECT a.is_regular,a.emp_id,ARRAY_TO_STRING(ARRAY[INITIALS, a.F_NAME, a.M_NAME,a.L_NAME], ' ') fullname,a.cur_basic_salary,to_char(a.dob, 'DD-Mon-YYYY') as dob,to_char(a.dos, 'DD-Mon-YYYY') as dos,b.spn,b.spc,c.off_name  FROM emp_mast a,g_spc b,g_office c WHERE a.cur_spc=b.spc AND a.cur_off_code=c.off_code AND   emp_id=?");
            ps.setString(1, apply_hrmsid);
            rs = ps.executeQuery();

            String fullname = "";
            String cur_basic_salary = "";
            String spn = "";
            String spc = "";
            String off_name = "";
            String dob = "";
            String dos = "";
            String netsalary = "";
            if (rs.next()) {
                fullname = rs.getString("fullname");
                cur_basic_salary = rs.getString("cur_basic_salary");
                spn = rs.getString("spn");
                spc = rs.getString("spc");
                off_name = rs.getString("off_name");
                netsalary = getNetPay(apply_hrmsid, cmonth, cyear) + "";
                if (rs.getString("is_regular").equals("Y")) {
                    jobtype = "Permanent";
                } else {
                    jobtype = "Temporary";
                }
                dob = rs.getString("dob");
                dos = rs.getString("dos");

            }

            Font f1 = new Font();
            f1.setSize(10);
            f1.setFamily("Times New Roman");
            Font hdrTextFont = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD, BaseColor.BLACK);
            Font hdrTextFont1 = new Font(Font.FontFamily.HELVETICA, 6, Font.BOLD, BaseColor.BLACK);
            Font dataHdrFont = new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD, BaseColor.BLACK);
            Font dataValFont = new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK);

            PdfPTable table = null;
            table = new PdfPTable(1);
            table.setWidths(new int[]{5});
            table.setWidthPercentage(100);
            PdfPCell cell = null;

            cell = new PdfPCell(new Phrase("Application form for Advance for the purpose of Motor car/motor cycle/Moped/personal computer", hdrTextFont));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            document.add(table);

            PdfPTable table1 = new PdfPTable(2);
            table1.setWidths(new int[]{3, 5});
            table1.setWidthPercentage(100);
            PdfPCell datacell;

            datacell = new PdfPCell(new Paragraph(" ", dataHdrFont));
            datacell.setBorder(Rectangle.NO_BORDER);
            datacell.setColspan(2);
            datacell.setFixedHeight(15);
            datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table1.addCell(datacell);

            document.add(table1);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }

    }

    @Override
    public LoanForm getLoanDetails(int loanid) {
        LoanForm lform = new LoanForm();
        String fullName = "";
        String employeeId = null;
        Connection conn = null;
        PreparedStatement statement = null;
        PreparedStatement pstmt = null;
        PreparedStatement pstmt2 = null;
        ResultSet result = null;
        ResultSet result2 = null;
        ResultSet rs = null;
        ResultSet result1 = null;
        ResultSet rs1 = null;
        ResultSet rs2 = null;
        String selectQry = "";
        ArrayList outList = new ArrayList();
        ArrayList outList1 = new ArrayList();
        try {
            conn = dataSource.getConnection();
            String SQL = "SELECT * FROM hw_loan_sanction_details WHERE loan_id=?";
            pstmt = conn.prepareStatement(SQL);
            pstmt.setInt(1, loanid);
            result = pstmt.executeQuery();
            while (result.next()) {
                lform.setLoanId(loanid);

                lform.setAccountType(result.getString("accountno"));
                String accountType = result.getString("accountno");
                if (accountType.toUpperCase().equals("PRAN")) {
                    lform.setPranNo(result.getString("pranno"));
                } else {
                    lform.setPranNo("");
                }
                if (accountType.toUpperCase().equals("GPF")) {
                    lform.setGpfno(result.getString("gpfno"));
                } else {
                    lform.setGpfno("");
                }
                lform.setName(result.getString("name"));
                lform.setDesignation(result.getString("designation"));
                lform.setOffaddress(result.getString("office_name"));
                lform.setDob(result.getString("dob"));
                lform.setDos(result.getString("dos"));
                lform.setBasicsalary(result.getString("basic_pay"));
                lform.setGp(result.getString("gp"));
                lform.setEmpType(result.getString("emptype"));
                lform.setAddress(result.getString("address"));
                lform.setLoanType(result.getString("loan_type"));
                lform.setInterestType(result.getString("interesttype"));
                lform.setSamount(result.getString("sanction_amount"));
                lform.setReleaseAmount(result.getString("release_amount"));
                //  lform.setPrincipalAmount(result.getInt("prinicipal_installment_amt"));
                lform.setPrincipalInstAmount(result.getInt("prinicipal_installment_amt"));
                //  lform.setPrincipalAmount(result.getInt("prinicipal_installment"));
                lform.setPrincipalInstallment(result.getInt("prinicipal_installment"));
                lform.setInterestAmount(result.getString("interest_installment_amt"));
                lform.setEmiInterest(result.getInt("interest_installment"));
                lform.setRateInterest(result.getString("rate_interest"));

                lform.setLastInstallment(result.getString("last_installment_amt"));
                lform.setPenalRate(result.getString("penal_rate_interest"));
                lform.setMoratoriumRequired(result.getString("moratorium_required"));
                lform.setMoratoriumPeriod(result.getString("moratorium_period"));
                lform.setInsuranceFlag(result.getString("insurance_flag"));
                lform.setRecDate(result.getString("recovery_sdate"));
                lform.setSanOperatorDDO(result.getString("sanction_operator_loginid"));
                lform.setSanOperatorLoginId(result.getString("sanction_operator_ddo"));

            }

            return lform;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(result, statement);
            DataBaseFunctions.closeSqlObjects(conn);
        }
        return lform;
    }

    @Override
    public Users getLoanEmpDetail(String empId) {
        Users empBean = new Users();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = null;
        try {
            con = this.dataSource.getConnection();
            sql = "SELECT A.bank_acc_no,A.mobile, C.POST, E.off_en,E.ddo_code,F.ifsc_code,ARRAY_TO_STRING(ARRAY[INITIALS, A.F_NAME, A.M_NAME,A.L_NAME], ' ') fullname,A.is_regular"
                    + " ,A.gpf_no,A.gp,acct_type ,A.cur_basic_salary,to_char(A.dob, 'DD-Mon-YYYY') as dob,to_char(A.dos, 'DD-Mon-YYYY') as dos,address FROM   EMP_MAST A "
                    + " LEFT outer join G_SPC B on A.CUR_SPC=B.SPC  "
                    + "	LEFT outer join G_BRANCH F on A.branch_code=F.branch_code "
                    + "	LEFT outer join emp_address H on H.emp_id=A.emp_id AND address_type='PRESENT' "
                    + " left outer join g_post C on B.gpc=C.post_code "
                    + "	LEFT OUTER JOIN g_department GD ON C.department_code = GD.department_code, G_OFFICE E  "
                    + "	WHERE A.CUR_OFF_CODE=E.OFF_CODE AND A.EMP_ID=? ";
            ps = con.prepareStatement(sql);
            ps.setString(1, empId);
            rs = ps.executeQuery();
            if (rs.next()) {
                empBean.setFullName(rs.getString("fullname"));
                empBean.setBankAccNo(rs.getString("bank_acc_no"));
                empBean.setMobile(rs.getString("mobile"));
                empBean.setDdoCode(rs.getString("ddo_code"));
                empBean.setIfmsCode(rs.getString("ifsc_code"));
                empBean.setGpfno(rs.getString("gpf_no"));
                empBean.setGp(rs.getString("gp"));
                empBean.setAddress(rs.getString("address"));
                empBean.setBasicsalry(rs.getString("cur_basic_salary"));
                empBean.setIsRegular(rs.getString("is_regular"));
                empBean.setEmpDob(rs.getString("dob"));
                empBean.setEmpDos(rs.getString("dos"));
                empBean.setAcctType(rs.getString("acct_type"));

                empBean.setPostname(rs.getString("POST"));
                empBean.setOffname(rs.getString("off_en"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return empBean;
    }

    @Override
    public String getxmlLoansanction(String folderPath, int taskid, int loanid, String hrmsid) {
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        ResultSet rs1, rs2 = null;
        ArrayList outList = new ArrayList();
        String stm, stm1 = null;
        String fnamereturn = "";
        //int month = 9;
        // int year = 2012;
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calobj = Calendar.getInstance();
        String cdate = df.format(calobj.getTime());

        try {
            con = dataSource.getConnection();
            stm = "SELECT *,to_date(bill_date::TEXT,'YYYY-MM-DD') as fbill_date,to_date(sanction_date::TEXT,'YYYY-MM-DD') as fsanction_date FROM hw_loan_sanction_details  WHERE taskid=? AND apply_loanid=? AND bill_create_id=? ";
            ps = con.prepareStatement(stm);
            ps.setInt(1, taskid);
            ps.setInt(2, loanid);
            ps.setString(3, hrmsid);
            rs1 = ps.executeQuery();
            while (rs1.next()) {
                DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
                org.w3c.dom.Document doc = docBuilder.newDocument();
                org.w3c.dom.Element rootElement = doc.createElement("bulkPayment");
                doc.appendChild(rootElement);
                String tslno = "35";
                tslno = StringUtils.leftPad(tslno, 3, "0");
                /**
                 * *************************** File Details
                 * *********************
                 */
                org.w3c.dom.Element row = doc.createElement("fileDetail");
                org.w3c.dom.Element element = doc.createElement("fileType");
                element.appendChild(doc.createTextNode("EPB"));
                row.appendChild(element);
                element = doc.createElement("intgCode");
                element.appendChild(doc.createTextNode("09"));
                row.appendChild(element);
                String scodeloanId = rs1.getString("loan_id");
                String scode = StringUtils.leftPad(scodeloanId, 10, "0");

                String scode_eight = StringUtils.leftPad(scodeloanId, 8, "0");

                element = doc.createElement("serviceCode");
                element.appendChild(doc.createTextNode("01"));
                row.appendChild(element);

                element = doc.createElement("fileRefId");
                element.appendChild(doc.createTextNode(scode));
                row.appendChild(element);

                element = doc.createElement("fileSlNo");
                element.appendChild(doc.createTextNode(tslno));
                row.appendChild(element);
                element = doc.createElement("fileDate");
                element.appendChild(doc.createTextNode(cdate));
                row.appendChild(element);
                rootElement.appendChild(row);

                /**
                 * *************************** Bill Details
                 * *********************
                 */
                org.w3c.dom.Element row1 = doc.createElement("billDetail");
                org.w3c.dom.Element element1 = doc.createElement("billNumber");
                element1.appendChild(doc.createTextNode(rs1.getString("bill_no")));
                row1.appendChild(element1);
                element1 = doc.createElement("billDate");
                element1.appendChild(doc.createTextNode(rs1.getString("fbill_date")));
                row1.appendChild(element1);
                element1 = doc.createElement("billTypeId");
                element1.appendChild(doc.createTextNode(rs1.getString("bill_id")));
                row1.appendChild(element1);
                element1 = doc.createElement("ddoCode");
                element1.appendChild(doc.createTextNode(rs1.getString("ddo_code")));
                row1.appendChild(element1);
                element1 = doc.createElement("grossAmount");
                element1.appendChild(doc.createTextNode(rs1.getString("bill_amount") + ".00"));
                row1.appendChild(element1);
                element1 = doc.createElement("netAmount");
                element1.appendChild(doc.createTextNode(rs1.getString("bill_amount") + ".00"));
                row1.appendChild(element1);
                element1 = doc.createElement("noOfBenf");
                element1.appendChild(doc.createTextNode("1"));
                row1.appendChild(element1);
                element1 = doc.createElement("sanctionNo");
                element1.appendChild(doc.createTextNode(rs1.getString("sanction_no")));
                row1.appendChild(element1);

                element1 = doc.createElement("sanctionDate");
                element1.appendChild(doc.createTextNode(rs1.getString("fsanction_date")));
                row1.appendChild(element1);
                rootElement.appendChild(row1);

                /**
                 * *************************** hoaBreakups Details
                 * *********************
                 */
                org.w3c.dom.Element row3 = doc.createElement("hoaBreakups");
                org.w3c.dom.Element row3child = doc.createElement("hoaBreakup");

                org.w3c.dom.Element element3 = doc.createElement("hoa");
                element3.appendChild(doc.createTextNode(rs1.getString("chat_account")));
                row3child.appendChild(element3);

                element3 = doc.createElement("amount");
                element3.appendChild(doc.createTextNode(rs1.getString("bill_amount") + ".00"));
                row3child.appendChild(element3);

                row3.appendChild(row3child);

                rootElement.appendChild(row3);

                /**
                 * *********************************************************
                 */
                org.w3c.dom.Element row5Parent = doc.createElement("byTransfers");

                rootElement.appendChild(row5Parent);

                /**
                 * *********************************************************
                 */
                org.w3c.dom.Element row6Parent = doc.createElement("billAbstracts");

                rootElement.appendChild(row6Parent);

                //  rootElement.appendChild(row4);
                /**
                 * * *************************** Loan Details *
                 * *********************
                 */
                org.w3c.dom.Element row2Parent = doc.createElement("loanDetails");
                org.w3c.dom.Element row2 = doc.createElement("loanDetail");

                org.w3c.dom.Element element2 = doc.createElement("benfId");
                element2.appendChild(doc.createTextNode(scode_eight));
                row2.appendChild(element2);
                String empType = "";
                String empstatus = "";
                if (rs1.getString("emptype").equals("Permanent")) {
                    empstatus = "P";
                } else {
                    empstatus = "T";
                }

                if (rs1.getString("accountno").equals("GPF")) {
                    empType = "2";
                    element2 = doc.createElement("employeeType");
                    element2.appendChild(doc.createTextNode(empType));
                    row2.appendChild(element2);

                    element2 = doc.createElement("gpfSeries");
                    element2.appendChild(doc.createTextNode(rs1.getString("gpf_series")));
                    row2.appendChild(element2);

                    element2 = doc.createElement("gpfAccountNo");
                    element2.appendChild(doc.createTextNode(rs1.getString("gpf_accountno")));
                    row2.appendChild(element2);

                    //	loanvalue.setJobType("Permanent");
                } else {
                    empType = "3";
                    element2 = doc.createElement("employeeType");
                    element2.appendChild(doc.createTextNode(empType));
                    row2.appendChild(element2);

                    element2 = doc.createElement("pran");
                    element2.appendChild(doc.createTextNode(rs1.getString("pranno")));
                    row2.appendChild(element2);

                }
                element2 = doc.createElement("loanId");
                element2.appendChild(doc.createTextNode(rs1.getString("ifms_loanid")));
                row2.appendChild(element2);

                element2 = doc.createElement("employeeName");
                element2.appendChild(doc.createTextNode(rs1.getString("name")));
                row2.appendChild(element2);
                //String designation=rs1.getString("designation");
                // designation=designation.substring(0,50);

                element2 = doc.createElement("designation");
                element2.appendChild(doc.createTextNode(rs1.getString("designation")));
                row2.appendChild(element2);
                element2 = doc.createElement("demandNo");
                element2.appendChild(doc.createTextNode(rs1.getString("demand_no")));
                row2.appendChild(element2);
                element2 = doc.createElement("majorHead");
                element2.appendChild(doc.createTextNode(rs1.getString("major_head")));
                row2.appendChild(element2);

                element2 = doc.createElement("employmentStatus");
                element2.appendChild(doc.createTextNode(empstatus));
                row2.appendChild(element2);

                element2 = doc.createElement("securityTaken");
                element2.appendChild(doc.createTextNode("N"));
                row2.appendChild(element2);

                element2 = doc.createElement("pay");
                element2.appendChild(doc.createTextNode(rs1.getString("basic_pay") + ".00"));
                row2.appendChild(element2);

                element2 = doc.createElement("gradePay");
                element2.appendChild(doc.createTextNode(rs1.getString("gp") + ".00"));
                row2.appendChild(element2);

                element2 = doc.createElement("amountOfAdvance");
                element2.appendChild(doc.createTextNode(rs1.getString("bill_amount") + ".00"));
                row2.appendChild(element2);

                element2 = doc.createElement("sanctionNo");
                element2.appendChild(doc.createTextNode(rs1.getString("sanction_no")));
                row2.appendChild(element2);

                element2 = doc.createElement("sanctionDate");
                element2.appendChild(doc.createTextNode(rs1.getString("fsanction_date")));
                row2.appendChild(element2);

                element2 = doc.createElement("releaseOrderNo");
                element2.appendChild(doc.createTextNode(rs1.getString("sanction_no")));
                row2.appendChild(element2);

                element2 = doc.createElement("releaseOrderDate");
                element2.appendChild(doc.createTextNode(rs1.getString("fsanction_date")));
                row2.appendChild(element2);

                element2 = doc.createElement("hrmsId");
                element2.appendChild(doc.createTextNode(rs1.getString("loanee_id")));
                row2.appendChild(element2);

                // String applyloanid = rs1.getInt("apply_loanid") + "";
                // applyloanid = StringUtils.leftPad(applyloanid, 8, "0");
                element2 = doc.createElement("hrmsLoanId");
                element2.appendChild(doc.createTextNode(scode_eight));
                row2.appendChild(element2);

                row2Parent.appendChild(row2);
                rootElement.appendChild(row2Parent);

                /**
                 * * *************************** beneficiaries Details *
                 * *********************
                 */
                org.w3c.dom.Element row4Parent = doc.createElement("beneficiaries");
                org.w3c.dom.Element row4 = doc.createElement("beneficiary");

                org.w3c.dom.Element element4 = doc.createElement("benfId");
                element4.appendChild(doc.createTextNode(scode_eight));
                row4.appendChild(element4);

                element4 = doc.createElement("name");
                element4.appendChild(doc.createTextNode(rs1.getString("name")));
                row4.appendChild(element4);

                element4 = doc.createElement("accountNo");
                element4.appendChild(doc.createTextNode(rs1.getString("bank_account")));
                row4.appendChild(element4);

                element4 = doc.createElement("ifsc");
                element4.appendChild(doc.createTextNode(rs1.getString("ifsc_code")));
                row4.appendChild(element4);

                element4 = doc.createElement("mobileNo");
                element4.appendChild(doc.createTextNode(rs1.getString("mobile")));
                row4.appendChild(element4);

                element4 = doc.createElement("address");
                element4.appendChild(doc.createTextNode(rs1.getString("address")));
                row4.appendChild(element4);

                element4 = doc.createElement("accountType");
                element4.appendChild(doc.createTextNode("S"));
                row4.appendChild(element4);

                element4 = doc.createElement("amount");
                element4.appendChild(doc.createTextNode(rs1.getString("bill_amount") + ".00"));
                row4.appendChild(element4);

                element4 = doc.createElement("sanctionNo");
                element4.appendChild(doc.createTextNode(rs1.getString("sanction_no")));
                row4.appendChild(element4);

                element4 = doc.createElement("sanctionDate");
                element4.appendChild(doc.createTextNode(rs1.getString("fsanction_date")));
                row4.appendChild(element4);

                row4Parent.appendChild(row4);
                rootElement.appendChild(row4Parent);

                String fname = "EPB" + "09" + "01" + scode + tslno + ".xml";
                fnamereturn = "EPB" + "09" + "01" + scode + tslno;
                // String fnamezip = "EPB|09|01|0000000001|001|";
                File f = new File(folderPath + fname);

                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                DOMSource source = new DOMSource(doc);
                StreamResult result = new StreamResult(f);
                transformer.transform(source, result);

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(ps, ps1);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return fnamereturn;
    }

    public static String getempNumberFromGPF(String gpfno) {
        String numgpfno = "";
        for (int i = 0; i < gpfno.length(); i++) {
            if (isNumeric(gpfno.substring(i, i + 1)) == true) {
                numgpfno = numgpfno + gpfno.substring(i, i + 1);
            }
        }
        return numgpfno;
    }

    public static boolean isNumeric(String str) {
        try {
            double d = Double.parseDouble(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    @Override
    public List getLoanAuthoritySanctionList(String empId) {
        Connection con = null;

        Statement stmt = null;
        ResultSet rs = null;

        LoanASanctionBean lBean = null;
        ArrayList alist = new ArrayList();
        try {
            con = this.dataSource.getConnection();

          String sql = "SELECT * FROM hw_loan_sanction_details a WHERE approver_id = '" + empId + "'"
                   + " OR loan_sanction_login_id = '" + empId + "' OR issue_letter_login_id = '" + empId + "'  OR bill_create_id = '" + empId + "' AND sanction_no IS NOT NULL";
           // String sql = "SELECT * FROM hw_loan_sanction_details a WHERE  sanction_no IS NOT NULL";
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                lBean = new LoanASanctionBean();
                String status = "";
                String btnName = "";
                lBean.setDesignation(rs.getString("designation"));
                lBean.setName(rs.getString("name"));
                lBean.setGpfNo(rs.getString("gpfno"));
                lBean.setLoanId(rs.getInt("loan_id") + "");
                lBean.setLoaneeId(rs.getString("loanee_id"));
                lBean.setLoanType(rs.getString("loan_type"));
                lBean.setIfmsLoanId(rs.getString("ifms_loanid"));
                lBean.setTaskId(rs.getString("taskid"));
                if (rs.getString("approver_id") != null && !rs.getString("approver_id").equals("")) {
                    status = "APPROVED";
                    btnName = "A";
                }
                if (rs.getString("loan_sanction_login_id") != null && !rs.getString("loan_sanction_login_id").equals("")) {
                    status = "SANCTIONED";
                    btnName = "S";
                }
                if (rs.getString("bill_create_id") != null && !rs.getString("bill_create_id").equals("")) {
                    status = "BILL ID GENERATED";
                    btnName = "B";
                }
                lBean.setStatus(status);
                lBean.setBtnName(btnName);
                alist.add(lBean);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return alist;
    }

    @Override
    public String getLoanType(int taskId) {
        String loanType = "";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = null;
        try {
            con = this.dataSource.getConnection();
            sql = "SELECT loan_type FROM hw_loan_sanction_details WHERE taskid = ?";
            ps = con.prepareStatement(sql);
            ps.setInt(1, taskId);
            rs = ps.executeQuery();
            if (rs.next()) {
                loanType = rs.getString("loan_type");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return loanType;
    }

    @Override
    public void viewHBAPDFfunc(Document document, int taskId, String hrmsid) {
        LoanForm loanvalue = new LoanForm();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        PreparedStatement ps1 = null;

        Calendar now = Calendar.getInstance();

        int cyear = now.get(Calendar.YEAR);
        int nextYear = cyear + 1;
        String fyear = cyear + " - " + nextYear;

        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("select * from hw_loan_sanction_details HD INNER JOIN hw_emp_hba_loan HL ON HD.taskid = HL.taskid WHERE HL.taskid = ?");
            ps.setInt(1, taskId);

            rs = ps.executeQuery();
            rs.next();
            Font f1 = new Font();
            f1.setSize(10);
            f1.setFamily("Times New Roman");
            Font hdrTextFont = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD, BaseColor.BLACK);
            Font hdrTextFont1 = new Font(Font.FontFamily.HELVETICA, 6, Font.BOLD, BaseColor.BLACK);
            Font dataHdrFont = new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD, BaseColor.BLACK);
            Font dataValFont = new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK);
            final Font pFont = FontFactory.getFont("Arial", 10, Font.NORMAL);
            final Font bFont = FontFactory.getFont("Arial", 10, Font.BOLD);
            final Font bottomRule = FontFactory.getFont("Arial", 11, Font.NORMAL);

            PdfPTable table = null;

            table = new PdfPTable(1);
            table.setWidths(new int[]{5});
            table.setWidthPercentage(100);

            PdfPCell cell = null;

            cell = new PdfPCell(new Phrase("GOVERNMENT OF ODISHA", hdrTextFont));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(rs.getString("office_name"), hdrTextFont));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            document.add(table);

            PdfPTable table1 = new PdfPTable(2);
            table1.setWidths(new int[]{5, 5});
            table1.setWidthPercentage(100);
            PdfPCell datacell;
            
             table = new PdfPTable(2);
            table.setWidths(new int[]{3, 2});
            table.setWidthPercentage(100);

            cell = new PdfPCell(new Phrase("\nNo. "+rs.getString("sanction_no"), f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
           
            cell = new PdfPCell(new Phrase("Dated:"+CommonFunctions.getFormattedOutputDate1(rs.getDate("sanction_date")), f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
             document.add(table);

            Users ue = getLoanEmpDetail(rs.getString("issue_letter_login_id"));

            String pointStr1 = "From\n          " + ue.getFullName() + ",\n          " + ue.getPostname() + "\nTo\n          The Accountant General (A & E), Odisha,\n          Bhubaneswar.";
            Paragraph point2 = new Paragraph(pointStr1, pFont);
            document.add(point2);
            String pointStr2 = "Sub: Grant of House Building Advance to " + rs.getString("name") + ", " + rs.getString("designation") + ", " + rs.getString("office_name");
            Paragraph point3 = new Paragraph(pointStr2, pFont);
            document.add(point3);
            //Chunk c1 = new Chunk(rs.getInt("release_amount"), ):
            String pointStr = "Sir,\n          I am directed to convey the sanction of Governor under rule 3(a) of rules to regulate the grant of advance to the State Government servants for building etc. of houses laid down in Finance Department Resolution No. 2124/F.,"
                    + " dated 22.06.1959 read with Finance Deptt. Office Memorandum No. 4470/F., dated 02.02.2010 to the grant of an advance of " + rs.getInt("sanction_amount") + "/- (" + CommonFunctions.convertNumber(rs.getInt("sanction_amount")) + ") " + " only to "
                    + rs.getString("name") + " , " + rs.getString("designation") + " for purchase of a plot of land and construction of a residential house over the said land.";
            Paragraph point1 = new Paragraph(pointStr, pFont);
            document.add(point1);

            String pointStr4 = "\n2. He/she may draw an amount of Rs " + rs.getInt("release_amount") + "/- (" + CommonFunctions.convertNumber(rs.getInt("release_amount")) + ") " + " only as 1st instalment equal to 50% of the total advance on execution of the agreement deed in favour of Government of Odisha duly registered in the office of the Local Sub-Registrar for payment of the advance";
            Paragraph point4 = new Paragraph(pointStr4, pFont);
            document.add(point4);

            String msg1 = "\n3. He/She may draw amount of Rs " + rs.getInt("release_amount") + "/- (" + CommonFunctions.convertNumber(rs.getInt("release_amount")) + ") " + " only as 2nd instalment of the advance on submission of utilisation certificate in support of the deposit of the amount released towards 1st instalment with appropriate authority.";
            Paragraph point5 = new Paragraph(msg1, pFont);
            document.add(point5);

            String msg5 = "\n4. The advance shall carry " + rs.getString("interesttype") + " interest @ " + rs.getString("rate_interest") + "% per annum from the date and the advance is drawn as provided under rule 6 of the rules referred to above.";
            Paragraph point6 = new Paragraph(msg5, pFont);
            document.add(point6);

            String msg6 = "\n5. The advance shall be recovered in " + rs.getString("prinicipal_installment") + " (" + CommonFunctions.convertNumber(rs.getInt("prinicipal_installment")) + ") " + " consecutive monthly instalments  @ Rs. " + rs.getInt("prinicipal_installment_amt") + "/- (" + CommonFunctions.convertNumber(rs.getInt("prinicipal_installment_amt")) + ") " + " only  and the interest shall be recovered in " + rs.getString("interest_installment") + " (" + CommonFunctions.convertNumber(rs.getInt("interest_installment")) + ") " + " instalments.";
            Paragraph point7 = new Paragraph(msg6, pFont);
            document.add(point7);

            String msg7 = "\n6. If any advance remains outstanding against him it is open to Government to recover the same along with the interest thereon from his D.C.R. Gratuity or and to enforce the security of the mortgage at any time thereafter and recover the balance of the dues together with the interest by the sale of the house or in such other manner as may be permissible under law.";
            Paragraph point8 = new Paragraph(msg7, pFont);
            document.add(point8);

            String msg8 = "\n7. The recovery of the advance shall commence from the next month after the date on which the first instalment of the advance is paid to him. He may repay the advance in shorter period if he so desires.";
            Paragraph point9 = new Paragraph(msg8, pFont);
            document.add(point9);

            String msg9 = "\n8. The recovery of advance shall be effected through the monthly pay/ leave salary bill of " + rs.getString("name") + ", " + rs.getString("designation") + "  "
                    + "These recoveries will not be held up or postponed except with the prior concurrence of the Government. If he ceases to be in Government service for any reason other than normal retirement/superannuation or if he dies before payment of the loan in full, the entire outstanding amount together with the interest shall be payable to Government forthwith.";
            Paragraph point10 = new Paragraph(msg9, pFont);
            document.add(point10);

            String msg10 = "\n9. The advance sanctioned as per the F.D.O.M. 4470/F dt.02.02.2010.";
            Paragraph point11 = new Paragraph(msg10, pFont);
            document.add(point11);

            String msg11 = "\n10. The house and the plot of land on which the house is proposed to be constructed shall remain the property of Government until the advance together with the interest due is fully repaid to Government.";
            Paragraph point12 = new Paragraph(msg11, pFont);
            document.add(point12);

            String msg12 = "\n11.  " + rs.getString("name") + ", " + rs.getString("designation") + ", is a permanent Government servant and his date of birth is " + CommonFunctions.getFormattedOutputDate1(rs.getDate("dob")) + ".";
            Paragraph point13 = new Paragraph(msg12, pFont);
            document.add(point13);

            String msg2 = "\n4. The charge is debitable to Demand No 5- 7610 - Loans to Government Servants etc. State Plan (201)- House Building Advances "
                    + " 0825 -  Loans & Advances - 48016 - HBA(NORMAL) Voted in the Budget Estimate for '" + CommonFunctions.getFinancialYear(rs.getString("sanction_date")) + "'.";
            Paragraph point14 = new Paragraph(msg2, pFont);
            document.add(point14);

            Paragraph blank = new Paragraph("\nYours Faithfully     \n\n\n" + ue.getPostname() + " to Government\n\n", bottomRule);
            blank.setAlignment(Element.ALIGN_RIGHT);
            document.add(blank);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void viewGPFPDFfunc(Document document, int taskId, String hrmsid) {
        LoanForm loanvalue = new LoanForm();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        PreparedStatement ps1 = null;

        Calendar now = Calendar.getInstance();

        int cmonth = now.get(Calendar.MONTH);
        int cyear = now.get(Calendar.YEAR);

        int previousYear = cyear;
        if (cmonth <= 3) {
            previousYear = cyear - 1;
            //cyear = previousYear;

        }
        if (cmonth == 0) {
            cmonth = 12;
        }

        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("select * from hw_loan_sanction_details HD INNER JOIN hw_emp_gpf_loan HL ON HD.taskid = HL.taskid WHERE HL.taskid = ?");
            ps.setInt(1, taskId);

            rs = ps.executeQuery();
            rs.next();
            Font f1 = new Font();
            f1.setSize(10);
            f1.setFamily("Times New Roman");
            Font hdrTextFont = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD, BaseColor.BLACK);
            Font hdrTextFont1 = new Font(Font.FontFamily.HELVETICA, 6, Font.BOLD, BaseColor.BLACK);
            Font dataHdrFont = new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD, BaseColor.BLACK);
            Font dataValFont = new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK);
            final Font pFont = FontFactory.getFont("Arial", 10, Font.NORMAL);
            final Font bFont = FontFactory.getFont("Arial", 10, Font.BOLD);
            final Font bottomRule = FontFactory.getFont("Arial", 11, Font.NORMAL);

            PdfPTable table = null;

            table = new PdfPTable(1);
            table.setWidths(new int[]{5});
            table.setWidthPercentage(100);

            PdfPCell cell = null;

            cell = new PdfPCell(new Phrase("GOVERNMENT OF ODISHA", hdrTextFont));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(rs.getString("office_name"), hdrTextFont));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            document.add(table);
            
            table = new PdfPTable(2);
            table.setWidths(new int[]{3, 2});
            table.setWidthPercentage(100);

            cell = new PdfPCell(new Phrase("\nNo. "+rs.getString("sanction_no"), f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
           
            cell = new PdfPCell(new Phrase("Dated:"+CommonFunctions.getFormattedOutputDate1(rs.getDate("sanction_date")), f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
             document.add(table);

            PdfPTable table1 = new PdfPTable(2);
            table1.setWidths(new int[]{5, 5});
            table1.setWidthPercentage(100);
            PdfPCell datacell;

            Users ue = getLoanEmpDetail(rs.getString("issue_letter_login_id"));

            String pointStr1 = "From\n          " + ue.getFullName() + ",\n          " + ue.getPostname() + "\nTo\n          The Accountant General (A & E), Odisha,\n          Bhubaneswar.";
            Paragraph point2 = new Paragraph(pointStr1, pFont);
            document.add(point2);
            String pointStr2 = "Sub: " + rs.getString("loan_type") + " withdrawal from G.P.F. A/C No." + rs.getString("gpf_accountno") + " " + rs.getString("gpf_series") + " of " + rs.getString("name") + ", " + rs.getString("designation") + ", " + rs.getString("office_name");
            Paragraph point3 = new Paragraph(pointStr2, pFont);
            document.add(point3);
            //Chunk c1 = new Chunk(rs.getInt("release_amount"), ):
            String pointStr = "Sir,\n          I am directed to convey the sanction of Governor under Rule(V) of Appendix - F  of the G.P.F (O) Rules to the " + rs.getString("loan_type") + " withdrawal by  " + rs.getString("name") + ", " + rs.getString("designation") + ", "
                    + " a sum of RS " + rs.getInt("release_amount") + "/- (" + CommonFunctions.convertNumber(rs.getInt("release_amount")) + ") " + " only from "
                    + " from his/her G.P.F A/C No." + rs.getString("gpf_accountno") + " " + rs.getString("gpf_series") + " to enable hime to meet expenditure on " + rs.getString("purpose");
            Paragraph point1 = new Paragraph(pointStr, pFont);
            document.add(point1);

            String msg1 = "\n2.  The amount of withdrawal does not exceed 3/4th of the amount at the credit of " + rs.getString("name") + ", " + rs.getString("designation") + " in his/her G.P.F A/C No." + rs.getString("gpf_accountno") + " " + rs.getString("gpf_series") + ". His/her present basic Pay is " + rs.getString("basic_pay") + ". ";
            Paragraph point_msg1 = new Paragraph(msg1, pFont);
            document.add(point_msg1);

            String msg2 = "\n3. It is certified that " + rs.getString("name") + ", " + rs.getString("designation") + " has completed more than 20 Years of his/her Goverment Service till date. ";
            Paragraph point_msg2 = new Paragraph(msg2, pFont);
            document.add(point_msg2);

            String msg3 = "\n4. The balance at the credit of  " + rs.getString("name") + ", " + rs.getString("designation") + " as on " + CommonFunctions.getFormattedOutputDate1(rs.getDate("loan_apply_date")) + " is detailed below. ";
            Paragraph point_msg3 = new Paragraph(msg3, pFont);
            document.add(point_msg3);

            int totalBalnce = Integer.parseInt(rs.getString("credit_amount")) + Integer.parseInt(rs.getString("closing_balance"));
            int netblance = totalBalnce - Integer.parseInt((rs.getString("withdrawal_amount")));

            table = new PdfPTable(2);
            table.setWidths(new int[]{3, 2});
            table.setWidthPercentage(100);

            cell = new PdfPCell(new Phrase("\n1. Balance as per Account Slip for "+cyear, f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
           
            cell = new PdfPCell(new Phrase("Rs. "+rs.getString("closing_balance")+".00", f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

           

            cell = new PdfPCell(new Phrase("\n2. Subsequent deposit from "+rs.getString("credit_from") + " to " + rs.getString("credit_to"), f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
           
            cell = new PdfPCell(new Phrase("Rs. "+rs.getString("credit_amount")+".00", f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);


            cell = new PdfPCell(new Phrase("\n3. Total of Column 1 & 2 above", f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
           
            cell = new PdfPCell(new Phrase("Rs. "+totalBalnce + ".00", f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
           

            cell = new PdfPCell(new Phrase("\n4. Subsequent withdrawal if any", f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
           
            cell = new PdfPCell(new Phrase("Rs. "+rs.getString("withdrawal_amount")+ ".00", f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            

            cell = new PdfPCell(new Phrase("\n5.Net Balance", f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            
            cell = new PdfPCell(new Phrase("Rs. "+netblance + ".00", f1));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

           
            String msg4 = "\n5. A copy of the application along with Account Slip is enclosed. \n   As per Finance Department Memo No. 33765 dated 02.08.1999, the Annual Statement of drawal from G.P.F. Account of individual subscribers of G.A. Department for the year 2014-15 has been sent to the Account General (A & E), Odisha vide letter no 8897/Gen., dated 08.04.2015.";
            Paragraph point_msg4 = new Paragraph(msg4, pFont);
            document.add(point_msg4);
            
            Paragraph blank = new Paragraph("\nYours Faithfully     \n\n\n" + ue.getPostname() + " to Government\n\n", bottomRule);
            blank.setAlignment(Element.ALIGN_RIGHT);
            document.add(blank);
            
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
