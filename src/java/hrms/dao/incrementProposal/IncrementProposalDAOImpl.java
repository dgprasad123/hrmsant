/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.incrementProposal;

import hrms.SelectOption;
import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.common.GetUserAttribute;
import hrms.model.incrementProposal.EmpOrderBean;
import hrms.model.incrementProposal.EmpPayBean;
import hrms.model.incrementProposal.IncrementProposal;
import hrms.model.incrementProposal.ProposalAttr;
import hrms.model.login.Users;
import hrms.model.master.SubstantivePost;
import hrms.model.task.TaskBean;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 *
 * @author Surendra
 */
public class IncrementProposalDAOImpl implements IncrementProposalDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void saveProposal(IncrementProposal incr) {
        try {

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }

    @Override
    public void addProposedEmployee(String emp[], int year, int month, int propMastId) {
        Connection con = null;
        PreparedStatement ps2 = null;
        PreparedStatement ps = null;
        PreparedStatement ps3 = null;
        ResultSet rs = null;
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.DATE, 1);
        cal.set(Calendar.MONTH, month);

        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        String startTime = dateFormat.format(cal.getTime());
        try {
            Date outputDate = dateFormat.parse(startTime);
            con = this.dataSource.getConnection();
            if (emp != null) {
                ps2 = con.prepareStatement("UPDATE EMP_MAST SET DATE_OF_NINCR=?, PAY_DATE=? WHERE EMP_ID=?");
                for (int i = 0; i < emp.length; i++) {
                    ps2.setTimestamp(1, new Timestamp(outputDate.getTime()));
                    ps2.setTimestamp(2, new Timestamp(outputDate.getTime()));
                    ps2.setString(3, emp[i]);
                    ps2.execute();
                }
            }
            if (propMastId > 0) {
                ps = con.prepareStatement("INSERT INTO HW_INCREMENT_PROPOSAL_DETAIL (PROPOSAL_DETAIL_ID,PROPOSAL_ID,EMP_ID,CURRENT_BASIC,date_of_present_increment,date_of_present_pay, pay_commission, matrix_level, matrix_cell,pay_scale, previous_level, previous_cell) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)");
                ps3 = con.prepareStatement("SELECT PAY_DATE,DATE_OF_NINCR,CUR_BASIC_SALARY,CUR_SALARY,GP, pay_commission, matrix_level, matrix_cell FROM EMP_MAST WHERE EMP_ID=?");
                for (int i = 0; i < emp.length; i++) {
                    ps3.setString(1, emp[i]);
                    rs = ps3.executeQuery();
                    if (rs.next()) {
                        int maxCode = CommonFunctions.getMaxCode(con, "HW_INCREMENT_PROPOSAL_DETAIL", "PROPOSAL_DETAIL_ID");
                        ps.setInt(1, maxCode);
                        ps.setInt(2, propMastId);
                        ps.setString(3, emp[i]);
                        ps.setInt(4, rs.getInt("CUR_BASIC_SALARY"));
                        ps.setTimestamp(5, rs.getTimestamp("DATE_OF_NINCR"));
                        ps.setTimestamp(6, rs.getTimestamp("PAY_DATE"));
                        ps.setInt(7, rs.getInt("pay_commission"));
                        ps.setInt(8, rs.getInt("matrix_level"));
                        ps.setInt(9, rs.getInt("matrix_cell"));
                        ps.setString(10, rs.getString("CUR_SALARY"));
                        ps.setInt(11, rs.getInt("matrix_level"));
                        ps.setInt(12, rs.getInt("matrix_cell"));
                        ps.execute();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(ps2);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    public List getEmployeeList(String offCode, int year, int month) {
        Connection con = null;
        PreparedStatement ps2 = null;
        ResultSet rs = null;
        List emplist = new ArrayList();
        try {
            con = this.dataSource.getConnection();
            ps2 = con.prepareStatement("SELECT EMP_ID,GPF_NO,POST,DATE_OF_NINCR, ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMPNAME,F_NAME,PAY_DATE FROM EMP_MAST EMP "
                    + "   INNER JOIN G_SPC GSPC ON EMP.CUR_SPC=GSPC.SPC "
                    + "   INNER JOIN G_POST GPOST ON GSPC.GPC=GPOST.POST_CODE WHERE CUR_OFF_CODE=? and is_regular not in ('N','D','B') and "
                    + "   dep_code not in ('00','08','09','10','11','12','13') "
                    + "   and (if_retired='N' or if_retired='' or if_retired is null) "
                    + "   and GSPC.is_sanctioned='Y' AND GSPC.IFUCLEAN='N' "
                    + "   AND (SELECT COUNT(*) FROM hw_increment_proposal_detail WHERE emp_id = EMP.emp_id AND EXTRACT(YEAR FROM date_of_present_increment) = " + year + ") = 0 "
                    + "   ORDER BY F_NAME");
            ps2.setString(1, offCode);
            rs = ps2.executeQuery();
            Users emp = null;
            SubstantivePost sup = null;
            while (rs.next()) {
                emp = new Users();
                emp.setEmpId(rs.getString("EMP_ID"));
                emp.setFullName(rs.getString("EMPNAME"));
                emp.setGpfno(rs.getString("GPF_NO"));
                emp.setStrIncrDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("DATE_OF_NINCR")));
                emp.setPaydate(rs.getDate("PAY_DATE"));
                sup = new SubstantivePost();
                sup.setSpn(rs.getString("POST"));
                emp.setSubstantivePost(sup);
                emplist.add(emp);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs,ps2);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return emplist;
    }

    @Override
    public List proposalList(String offCode, int page, int rows) {
        Connection con = null;
        PreparedStatement ps2 = null;
        ResultSet rs = null;
        List li = new ArrayList();
        IncrementProposal incrprop = null;
        String[] monthName = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        int firstpage = (page > 1) ? ((page - 1) * rows) + 1 : 0;
        try {
            con = this.dataSource.getConnection();
            String verifyingAuthority = "";
            String approvingAuthority = "";
            String sql = "SELECT PROPOSAL_ID,is_order_updated,approving_authority, verifying_authority, OFF_CODE,PROPOSAL_FOR_MONTH, PROPOSAL_FOR_YEAR,approved_order_no,approved_order_date,TASK_ID,STATUS_NAME,last_updated_on, note FROM ("
                    + " SELECT PROPOSAL_ID,is_order_updated, OFF_CODE,PROPOSAL_FOR_MONTH,PROPOSAL_FOR_YEAR,approved_order_no,approved_order_date,approving_authority, verifying_authority,TASK_MASTER.TASK_ID,STATUS_ID,last_updated_on, note FROM ( "
                    + " SELECT PROPOSAL_ID,is_order_updated, OFF_CODE,PROPOSAL_FOR_MONTH,PROPOSAL_FOR_YEAR,approving_authority, verifying_authority, approved_order_no,approved_order_date,TASK_ID,last_updated_on FROM HW_INCREMENT_PROPOSAL_MASTER WHERE OFF_CODE=?) HW_INCREMENT_PROPOSAL_MASTER"
                    + " LEFT OUTER JOIN TASK_MASTER ON HW_INCREMENT_PROPOSAL_MASTER.TASK_ID=TASK_MASTER.TASK_ID) HW_INCREMENT_PROPOSAL_MASTER "
                    + " LEFT OUTER JOIN G_PROCESS_STATUS ON HW_INCREMENT_PROPOSAL_MASTER.STATUS_ID=G_PROCESS_STATUS.STATUS_ID ORDER BY last_updated_on DESC";
            //System.out.println("SQL:"+sql);
            ps2 = con.prepareStatement(sql);
            //System.out.println("Office Code:"+offCode);
            ps2.setString(1, offCode);

            rs = ps2.executeQuery();
            while (rs.next()) {
                verifyingAuthority = approvingAuthority = "";
                if (rs.getString("verifying_authority") != null && !rs.getString("verifying_authority").equals("")) {
                    Users ue = getIncrEmpDetail(rs.getString("verifying_authority"));
                    verifyingAuthority = "<strong>" + ue.getFullName() + "</strong><br />" + ue.getOffname();
                }
                if (rs.getString("approving_authority") != null && !rs.getString("approving_authority").equals("")) {
                    Users ue = getIncrEmpDetail(rs.getString("approving_authority"));
                    approvingAuthority = "<strong>" + ue.getFullName() + "</strong><br />" + ue.getOffname();
                }
                incrprop = new IncrementProposal();
                incrprop.setProposalId(rs.getInt("PROPOSAL_ID"));
                incrprop.setProposalMonth(rs.getInt("PROPOSAL_FOR_MONTH"));
                incrprop.setMonthasString(monthName[rs.getInt("PROPOSAL_FOR_MONTH") - 1]);
                incrprop.setProposalYear(rs.getInt("PROPOSAL_FOR_YEAR"));
                TaskBean tbean = new TaskBean();
                tbean.setTaskid(rs.getInt("TASK_ID"));
                incrprop.setTask(tbean);
                incrprop.setOrddate(CommonFunctions.getFormattedOutputDate1(rs.getDate("approved_order_date")));
                incrprop.setOrderno(rs.getString("approved_order_no"));
                incrprop.setLastUpdated(CommonFunctions.getFormattedOutputDate1(rs.getDate("last_updated_on")));
                incrprop.setProcessStatusName(rs.getString("STATUS_NAME"));
                incrprop.setNote(rs.getString("note"));
                incrprop.setIsOrderUpdated(rs.getString("is_order_updated"));
                incrprop.setaAuthorityName(approvingAuthority);
                incrprop.setvAuthorityName(verifyingAuthority);
                li.add(incrprop);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs,ps2);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return li;
    }

    public static String getFutureAmt(double basic, double gradePay) {

        //double gp1 = Double.parseDouble(gradePay);
        double incramt = (basic + gradePay) * 0.03;

        double total = incramt;
        DecimalFormat df = new DecimalFormat("0");
        String formate = df.format(total);

        String finalval = "";

        int lastdigit = Integer.parseInt(formate.substring(formate.length() - 1));

        if (lastdigit > 0) {
            String str = formate.substring(0, formate.length() - 1);
            str = str + 9;
            finalval = CommonFunctions.getNextString(str);
        }
        if (finalval == null || finalval.equals("")) {
            finalval = "0";
        }
        return finalval;
    }

    @Override
    public List getCells(int level) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        List li = new ArrayList();
        try {
            con = this.dataSource.getConnection();

            ps = con.prepareStatement("SELECT level_slno FROM pay_matrix_2017 WHERE gp_level = ? AND law_level not like 'J-%' ORDER BY level_slno");
            ps.setInt(1, level);
            rs = ps.executeQuery();
            while (rs.next()) {
                SelectOption so = new SelectOption();
                so.setLabel(rs.getInt("level_slno") + "");
                so.setValue(rs.getInt("level_slno") + "");
                li.add(so);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs,ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return li;
    }

    public static int getNewFutureAmt(int level, int cell, String empId, Connection con) {

        PreparedStatement ps = null;
        int futureSalary = 0;
        ResultSet rs = null;
        String sql = "SELECT amt FROM pay_matrix_2017 WHERE gp_level = ? AND level_slno = ? and law_level not like 'J-%' order by amt limit 1";
        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, level);
            ps.setInt(2, cell);
            rs = ps.executeQuery();
            while (rs.next()) {
                futureSalary = rs.getInt("amt");
            }
            //Now update emp level and cell
            /*sql = "UPDATE emp_mast SET matrix_level = ?, matrix_cell = ? WHERE emp_id = ?";
             ps = con.prepareStatement(sql);
             ps.setInt(1, level);
             ps.setInt(2, cell);
             ps.setString(3, empId);
             ps.execute();*/
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs,ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return futureSalary;
    }

    @Override
    public int getFianlIncrementPay(int level, int cell, int payCommission, String presentPay, String empId) {
        int futurePay = 0;
        Connection con = null;
        try {
            con = this.dataSource.getConnection();
            if (payCommission == 7) {
                futurePay = getNewFutureAmt(level, cell, empId, con);
            } else {
                Users ue = getIncrEmpDetail(empId);
                futurePay = Integer.parseInt(presentPay) + Integer.parseInt(getFutureAmt(Double.parseDouble(presentPay), ue.getGradepay()));
                System.out.println("Future Pay" + futurePay);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            DataBaseFunctions.closeSqlObjects(con);
        }
        return futurePay;
    }

    @Override
    public List getBenificiaryList(String offcode, int year) {
        List li = null;
        Connection con = null;
        PreparedStatement ps = null;
        String sql = "SELECT EMP_ID, FROM EMP_MAST WHERE CUR_OFF_CODE ='" + offcode + "'"
                + " AND EMP_ID NOT IN (SELECT EMP_ID FROM (SELECT PROPOSAL_ID FROM HW_INCREMENT_PROPOSAL_MASTER"
                + " WHERE OFF_CODE='" + offcode + "' AND PROPOSAL_FOR_YEAR=" + year + ") "
                + " HW_INCREMENT_PROPOSAL_MASTER "
                + " INNER JOIN HW_INCREMENT_PROPOSAL_DETAIL"
                + " ON HW_INCREMENT_PROPOSAL_MASTER.PROPOSAL_ID=HW_INCREMENT_PROPOSAL_DETAIL.PROPOSAL_ID)";
        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement(sql);
            ps.execute();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return li;
    }

    @Override
    public int saveProposalList(String offCode, String loggedinEmpId, String spc, int month, int year) {
        Connection con = null;
        PreparedStatement ps = null;
        int maxCode_MASTER = 0;

        Calendar cal = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        String startTime = dateFormat.format(cal.getTime());
        try {

            Date outputDate = dateFormat.parse(startTime);

            con = this.dataSource.getConnection();

            maxCode_MASTER = CommonFunctions.getMaxCode(con, "HW_INCREMENT_PROPOSAL_MASTER", "PROPOSAL_ID");
            ps = con.prepareStatement("INSERT INTO HW_INCREMENT_PROPOSAL_MASTER (PROPOSAL_ID,OFF_CODE,PROPOSAL_FOR_MONTH,PROPOSAL_FOR_YEAR,CREATED_BY,CREATED_BY_SPC,LAST_UPDATED_ON) VALUES (?,?,?,?,?,?,?)");
            ps.setInt(1, maxCode_MASTER);
            ps.setString(2, offCode);
            ps.setInt(3, (month + 1));
            ps.setInt(4, year);
            ps.setString(5, loggedinEmpId);
            ps.setString(6, spc);
            ps.setTimestamp(7, new Timestamp(new Date().getTime()));
            ps.execute();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(ps);
            DataBaseFunctions.closeSqlObjects(con);

        }
        return maxCode_MASTER;
    }

    @Override
    public int saveProposalDetailList(String emp[], String pcArr[], String mlArr[], String mcArr[], String wefArr[], int maxCode_MASTER, int year, int month) {
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement ps2 = null;
        PreparedStatement ps3 = null;
        ResultSet rs = null;
        int maxCode = 0;
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.DATE, 1);
        cal.set(Calendar.MONTH, month);

        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        String startTime = dateFormat.format(cal.getTime());
        try {
            Date outputDate = dateFormat.parse(startTime);
            con = this.dataSource.getConnection();
            if (emp != null) {

                ps = con.prepareStatement("INSERT INTO HW_INCREMENT_PROPOSAL_DETAIL (PROPOSAL_DETAIL_ID,PROPOSAL_ID,EMP_ID,CURRENT_BASIC,INCREMENT_AMT,INCREMENTED_BASIC,date_of_present_increment,date_of_present_pay, pay_commission, matrix_level, matrix_cell, pay_scale, previous_level, previous_cell, gp) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
                ps2 = con.prepareStatement("UPDATE EMP_MAST SET DATE_OF_NINCR=? WHERE EMP_ID=?");
                ps3 = con.prepareStatement("SELECT PAY_DATE,DATE_OF_NINCR,CUR_BASIC_SALARY,GP,CUR_SALARY, MATRIX_LEVEL, MATRIX_CELL FROM EMP_MAST WHERE EMP_ID=?");

                for (int i = 0; i < emp.length; i++) {
                    ps3.setString(1, emp[i]);
                    rs = ps3.executeQuery();
                    if (rs.next()) {

                        maxCode = CommonFunctions.getMaxCode(con, "HW_INCREMENT_PROPOSAL_DETAIL", "PROPOSAL_DETAIL_ID");
                        //getFianlIncrementPay(Integer.parseInt(mlArr[i]), Integer.parseInt(mcArr[i]), Integer.parseInt(pcArr[i]), (rs.getInt("CUR_BASIC_SALARY")+""), emp[i]);
                        //String finalincr = getFutureAmt(rs.getDouble("CUR_BASIC_SALARY"));
                        //String futurebasic = (Integer.parseInt(finalincr) + rs.getInt("CUR_BASIC_SALARY")) + "";
                        int finalIncrement = getFianlIncrementPay(Integer.parseInt(mlArr[i]), Integer.parseInt(mcArr[i]), Integer.parseInt(pcArr[i]), (rs.getInt("CUR_BASIC_SALARY") + ""), emp[i]);
                        int incrementAmount = finalIncrement - rs.getInt("CUR_BASIC_SALARY");
                        ps.setInt(1, maxCode);
                        ps.setInt(2, maxCode_MASTER);
                        ps.setString(3, emp[i]);
                        ps.setInt(4, rs.getInt("CUR_BASIC_SALARY"));
                        ps.setInt(5, incrementAmount);
                        ps.setInt(6, finalIncrement);
                        ps.setTimestamp(7, new Timestamp(new Date(wefArr[i]).getTime()));
                        ps.setTimestamp(8, rs.getTimestamp("PAY_DATE"));
                        ps.setInt(9, Integer.parseInt(pcArr[i]));
                        ps.setInt(10, Integer.parseInt(mlArr[i]));
                        ps.setInt(11, Integer.parseInt(mcArr[i]));
                        ps.setString(12, rs.getString("CUR_SALARY"));
                        ps.setInt(13, rs.getInt("matrix_level"));
                        ps.setInt(14, rs.getInt("matrix_cell"));
                        ps.setInt(15, rs.getInt("GP"));
                        ps.execute();

                        ps2.setTimestamp(1, new Timestamp(outputDate.getTime()));
                        ps2.setString(2, emp[i]);
                        ps2.execute();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(ps, ps2);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return maxCode_MASTER;
    }

    public List getEmployeeWisePostList(String offcode) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        List li = new ArrayList();
        SubstantivePost post = null;
        try {
            con = this.dataSource.getConnection();

            ps = con.prepareStatement("SELECT EMP_ID, SPC,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME"
                    + ", M_NAME,L_NAME], ' ') EMPNAME,POST FROM ( SELECT SPC,POST FROM (SELECT SPC"
                    + ",GPC FROM G_SPC WHERE OFF_CODE=?) G_SPC INNER JOIN G_POST"
                    + " ON G_SPC.GPC=G_POST.POST_CODE) G_SPC"
                    + " INNER JOIN EMP_MAST ON G_SPC.SPC=EMP_MAST.CUR_SPC ");
            ps.setString(1, offcode);
            rs = ps.executeQuery();
            while (rs.next()) {
                post = new SubstantivePost();
                post.setSpc(rs.getString("SPC"));
                post.setEmpname(rs.getString("EMPNAME"));
                post.setPostname(rs.getString("POST"));
                li.add(post);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs,ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return li;
    }

    @Override
    public void deleteSelectedProposalFromList(int proposaldetailId, String empId) {
        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = this.dataSource.getConnection();

            ps = con.prepareStatement("DELETE FROM HW_INCREMENT_PROPOSAL_DETAIL WHERE PROPOSAL_DETAIL_ID=?");
            ps.setInt(1, proposaldetailId);
            ps.execute();

            ps = con.prepareStatement("UPDATE EMP_MAST SET DATE_OF_NINCR =null WHERE EMP_ID=?");
            ps.setString(1, empId);
            ps.execute();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void deleteIncrementProposal(int proposalId) {
        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = this.dataSource.getConnection();

            ps = con.prepareStatement("DELETE FROM HW_INCREMENT_PROPOSAL_DETAIL WHERE proposal_id=?");
            ps.setInt(1, proposalId);
            ps.execute();

            ps = con.prepareStatement("DELETE FROM hw_increment_proposal_master WHERE proposal_id =?");
            ps.setInt(1, proposalId);
            ps.execute();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public List getProposedEmployeeList(String offcode, int year, int month) {
        List li = new ArrayList();
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        String sql = null;
        PreparedStatement ps = null;
        ResultSet rs1 = null;
        String futurePay = null;
        try {
            con = this.dataSource.getConnection();
            sql = "SELECT C.EMP_ID,C.GPF_NO,C.EMPNAME,C.CUR_BASIC_SALARY, PAY_COMMISSION,MATRIX_LEVEL, MATRIX_CELL,C.CUR_SALARY,C.GP,C.PAY_DATE,C.DATE_OF_NINCR,(C.DATE_OF_NINCR-INTERVAL '1 years') as previous_incr_date, C.CUR_SPC,F_NAME,D.GPC,E.POST FROM "
                    + "                     (SELECT EMP_ID,GPF_NO,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME,M_NAME,L_NAME], ' ') EMPNAME, PAY_COMMISSION,MATRIX_LEVEL, MATRIX_CELL"
                    + "                     ,F_NAME,CUR_BASIC_SALARY,CUR_SALARY,GP,PAY_DATE,DATE_OF_NINCR,CUR_SPC FROM EMP_MAST  WHERE CUR_OFF_CODE='" + offcode + "' AND "
                    + "                     extract(month from DATE_OF_NINCR) =" + month + " and extract(year from DATE_OF_NINCR) =" + year + "  AND EMP_ID NOT IN ( "
                    + "                     SELECT B.EMP_ID FROM HW_INCREMENT_PROPOSAL_MASTER A, HW_INCREMENT_PROPOSAL_DETAIL B "
                    + "                     WHERE A.PROPOSAL_ID=B.PROPOSAL_ID  "
                    + "                     AND A.OFF_CODE='" + offcode + "' AND A.PROPOSAL_FOR_MONTH =" + month + " AND A.PROPOSAL_FOR_YEAR=" + year
                    + "                    ))  C, G_SPC D, G_POST E WHERE C.CUR_SPC=D.SPC AND D.GPC=E.POST_CODE ORDER BY C.F_NAME";

            //System.out.println(sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);
            ProposalAttr pro = null;
            String empId = null;
            while (rs.next()) {
                pro = new ProposalAttr();
                futurePay = "";
                empId = "";
                pro.setProposaldetailId(0);
                pro.setEmpId(rs.getString("EMP_ID"));
                pro.setEmpname(rs.getString("EMPNAME"));
                pro.setGpfno(rs.getString("GPF_NO"));
                pro.setPost(rs.getString("POST"));
                pro.setNextincr(CommonFunctions.getFormattedOutputDate3(rs.getDate("DATE_OF_NINCR")));
                pro.setPayscale(rs.getString("CUR_SALARY"));
                pro.setPresentpay(((Double) Math.ceil(rs.getDouble("CUR_BASIC_SALARY"))).intValue() + "");
                String incramt = getFutureAmt(rs.getDouble("CUR_BASIC_SALARY"), rs.getDouble("gp"));
                int futurebasic = ((Double) Math.ceil(rs.getDouble("CUR_BASIC_SALARY") + Integer.parseInt(incramt))).intValue();
                pro.setPresentpaydate(CommonFunctions.getFormattedOutputDate3(rs.getDate("previous_incr_date")));
                pro.setPayCommission(rs.getString("PAY_COMMISSION"));
                pro.setMatrixCell(rs.getString("matrix_cell"));
                pro.setMatrixLevel(rs.getString("matrix_level"));
                if (rs.getString("PAY_COMMISSION") != null && rs.getString("PAY_COMMISSION").equals("7")) {
                    if (pro.getMatrixCell() != null && !pro.getMatrixCell().equals("") && pro.getMatrixLevel() != null && !pro.getMatrixLevel().equals("")) {
                        sql = "SELECT amt FROM pay_matrix_2017 WHERE gp_level = ? AND level_slno = ? and law_level not like 'J-%' order by amt limit 1";
                        ps = con.prepareStatement(sql);
                        ps.setInt(1, rs.getInt("matrix_level"));
                        ps.setInt(2, rs.getInt("matrix_cell"));
                        rs1 = ps.executeQuery();
                        while (rs1.next()) {
                            futurePay = rs1.getInt("amt") + "";
                        }

                    }

                } else {
                    //Users ue = getIncrEmpDetail(empId);
                    //futurePay = (Integer.parseInt(rs.getString("CUR_BASIC_SALARY")) + Integer.parseInt(getFutureAmt(Double.parseDouble(rs.getString("CUR_BASIC_SALARY")), ue.getGradepay()))) + "";
                    futurePay = futurebasic + "";
                    System.out.println("FuturePay:" + futurePay);
                }
                if (futurePay.equals("0")) {
                    futurePay = "TBD";
                }
                pro.setFuturepay(futurePay);
                li.add(pro);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs,st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return li;
    }

    @Override
    public List getFinalProposedList(int proposalId) {
        List li = new ArrayList();
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        Statement st1 = null;
        ResultSet rs1 = null;
        PreparedStatement ps = null;
        String sql = "";
        try {
            sql = "SELECT PROPOSAL_DETAIL_ID,EMP_ID,GPF_NO, order_number, memo_number, order_date, PAY_COMMISSION,MATRIX_LEVEL, MATRIX_CELL,EMPNAME,CUR_SALARY,CURRENT_BASIC,INCREMENT_AMT "
                    + ",INCREMENTED_BASIC,GP,PAY_DATE,DATE_OF_NINCR,F_NAME,POST,date_of_present_increment,(date_of_present_increment-INTERVAL '1 years') as previous_incr_date,date_of_present_pay FROM ( SELECT PROPOSAL_DETAIL_ID "
                    + ", HW_INCREMENT_PROPOSAL_DETAIL.order_number,HW_INCREMENT_PROPOSAL_DETAIL.memo_number, HW_INCREMENT_PROPOSAL_DETAIL.order_date, PAY_COMMISSION,MATRIX_LEVEL, MATRIX_CELL,EMP_ID,GPF_NO "
                    + ",EMPNAME,CURRENT_BASIC,CUR_SALARY,INCREMENT_AMT,INCREMENTED_BASIC,HW_INCREMENT_PROPOSAL_DETAIL.GP,PAY_DATE,DATE_OF_NINCR,F_NAME,CUR_SPC,GPC,date_of_present_increment "
                    + ",date_of_present_pay FROM (                SELECT EMP_MAST.EMP_ID, HW_INCREMENT_PROPOSAL_DETAIL.order_number, HW_INCREMENT_PROPOSAL_DETAIL.memo_number, HW_INCREMENT_PROPOSAL_DETAIL.order_date, HW_INCREMENT_PROPOSAL_DETAIL.PAY_COMMISSION, HW_INCREMENT_PROPOSAL_DETAIL.MATRIX_LEVEL "
                    + ", HW_INCREMENT_PROPOSAL_DETAIL.MATRIX_CELL,GPF_NO,CUR_SALARY,                ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMPNAME "
                    + ",               F_NAME,CUR_SPC,                GP,PAY_DATE,DATE_OF_NINCR,INCREMENTED_BASIC,CURRENT_BASIC,INCREMENT_AMT,PROPOSAL_DETAIL_ID,date_of_present_increment "
                    + ",date_of_present_pay FROM (               SELECT PROPOSAL_DETAIL_ID,order_number, order_date, memo_number, EMP_ID,CURRENT_BASIC,INCREMENT_AMT,INCREMENTED_BASIC,pay_commission, matrix_level, matrix_cell "
                    + ", date_of_present_increment,date_of_present_pay FROM HW_INCREMENT_PROPOSAL_DETAIL WHERE PROPOSAL_ID=" + proposalId + ") HW_INCREMENT_PROPOSAL_DETAIL "
                    + "INNER JOIN EMP_MAST ON HW_INCREMENT_PROPOSAL_DETAIL.EMP_ID=EMP_MAST.EMP_ID) HW_INCREMENT_PROPOSAL_DETAIL               "
                    + " LEFT OUTER JOIN G_SPC ON HW_INCREMENT_PROPOSAL_DETAIL.CUR_SPC=G_SPC.SPC) HW_INCREMENT_PROPOSAL_DETAIL               "
                    + " LEFT OUTER JOIN G_POST ON HW_INCREMENT_PROPOSAL_DETAIL.GPC=G_POST.POST_CODE ORDER BY F_NAME";

            //System.out.println(sql);
            con = this.dataSource.getConnection();
            st = con.createStatement();
            rs = st.executeQuery(sql);
            String futurePay = "";
            ProposalAttr pro = null;
            String payScale = "";
            String empId = "";
            while (rs.next()) {
                pro = new ProposalAttr();
                if (rs.getString("PAY_COMMISSION") != null && Integer.parseInt(rs.getString("PAY_COMMISSION")) == 6) {
                    // RS. 10000-325-15200/- WEF 29-APR-2020, FN
                    payScale = "RS. " + rs.getString("cur_salary") + "/-";

                }
                if (rs.getString("PAY_COMMISSION") != null && Integer.parseInt(rs.getString("PAY_COMMISSION")) == 7) {
                    payScale = " IN CELL " + rs.getInt("matrix_cell") + " OF LEVEL " + rs.getInt("matrix_level");
                }
                pro.setProposaldetailId(rs.getInt("PROPOSAL_DETAIL_ID"));
                pro.setEmpId(rs.getString("EMP_ID"));
                pro.setGpfno(rs.getString("GPF_NO"));
                pro.setEmpname(rs.getString("EMPNAME"));
                pro.setNextincr(CommonFunctions.getFormattedOutputDate3(rs.getDate("date_of_present_increment")));
                pro.setPresentpay(rs.getString("CURRENT_BASIC"));
                pro.setPayscale(payScale);
                pro.setGp(rs.getString("GP") + "");
                pro.setPresentpaydate(CommonFunctions.getFormattedOutputDate3(rs.getDate("previous_incr_date")));
                pro.setOrdno(rs.getString("order_number"));
                pro.setOrderDate(CommonFunctions.getFormattedOutputDate3(rs.getDate("order_date")));
                pro.setMemoNo(rs.getString("memo_number"));
                pro.setPost(rs.getString("POST"));
                pro.setPayCommission(rs.getString("PAY_COMMISSION"));
                pro.setMatrixCell(rs.getInt("matrix_cell") + "");
                pro.setMatrixLevel(rs.getInt("matrix_level") + "");
                empId = rs.getString("EMP_ID");
                if (rs.getString("PAY_COMMISSION") != null && rs.getString("PAY_COMMISSION").equals("7")) {
                    if (pro.getMatrixCell() != null && !pro.getMatrixCell().equals("") && pro.getMatrixLevel() != null && !pro.getMatrixLevel().equals("")) {
                        sql = "SELECT amt FROM pay_matrix_2017 WHERE gp_level = ? AND level_slno = ? and law_level not like 'J-%'";
                        ps = con.prepareStatement(sql);
                        ps.setInt(1, rs.getInt("matrix_level"));
                        ps.setInt(2, rs.getInt("matrix_cell"));
                        rs1 = ps.executeQuery();
                        while (rs1.next()) {
                            futurePay = rs1.getInt("amt") + "";
                        }

                    }

                } else {
                    Users ue = getIncrEmpDetail(empId);

                    futurePay = (Integer.parseInt(rs.getString("CURRENT_BASIC")) + Integer.parseInt(getFutureAmt(Double.parseDouble(rs.getString("CURRENT_BASIC")), ue.getGradepay()))) + "";
                }
                if (futurePay.equals("0")) {
                    futurePay = "TBD";
                }
                pro.setFuturepay(futurePay);
                li.add(pro);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return li;
    }

    @Override
    public void deleteProposalDetail(int propmastId) {
        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("DELETE FROM HW_INCREMENT_PROPOSAL_DETAIL WHERE PROPOSAL_ID=?");
            ps.setInt(1, propmastId);
            ps.execute();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void submitProposal(int propmastId, String loggedempid, String loggedspc, String authSpc, String authSpc1) {

        Connection con = null;
        PreparedStatement pst = null;
        try {
            con = this.dataSource.getConnection();

            Calendar cal = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
            String startTime = dateFormat.format(cal.getTime());
            Date initiatedDt = dateFormat.parse(startTime);
            //System.out.println("Auth SPC:"+authSpc+"Auth SPC1:"+authSpc1);
            //int mcode = CommonFunctions.getMaxCode(con, "TASK_MASTER", "TASK_ID");
            pst = con.prepareStatement("INSERT INTO TASK_MASTER(PROCESS_ID, INITIATED_BY, INITIATED_ON, STATUS_ID"
                    + ", PENDING_AT,APPLY_TO,INITIATED_SPC,PENDING_SPC) Values (?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            //pst.setInt(1, mcode);
            pst.setInt(1, 4);
            pst.setString(2, loggedempid);
            pst.setTimestamp(3, new Timestamp(initiatedDt.getTime()));
            pst.setInt(4, 15);
            pst.setString(5, GetUserAttribute.getEmpId(con, authSpc1));
            pst.setString(6, GetUserAttribute.getEmpId(con, authSpc1));
            pst.setString(7, loggedspc);
            pst.setString(8, authSpc1);
            pst.executeUpdate();
            ResultSet rs = pst.getGeneratedKeys();
            rs.next();
            int taskID = rs.getInt("TASK_ID");
            pst = con.prepareStatement("UPDATE HW_INCREMENT_PROPOSAL_MASTER SET TASK_ID=?"
                    + ", approved_authority = ?, approving_authority = ?, verifying_authority_spc = ?"
                    + ", verifying_authority = ? WHERE PROPOSAL_ID=?");
            pst.setInt(1, taskID);
            pst.setString(2, authSpc);
            pst.setString(3, GetUserAttribute.getEmpId(con, authSpc));
            pst.setString(4, authSpc1);
            pst.setString(5, GetUserAttribute.getEmpId(con, authSpc1));
            pst.setInt(6, propmastId);
            pst.execute();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void authorityAction(int propmastId, int statusid, String note, String authspc) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int taskid = 0;
        Calendar cal = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");

        try {
            String startTime = dateFormat.format(cal.getTime());
            Date actionDate = dateFormat.parse(startTime);
            String verifyingAuthoritySpc = "";
            String verifyingAuthority = "";
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("SELECT TASK_ID, approved_authority, approving_authority FROM HW_INCREMENT_PROPOSAL_MASTER WHERE PROPOSAL_ID=?");
            ps.setInt(1, propmastId);
            rs = ps.executeQuery();
            if (rs.next()) {
                taskid = rs.getInt("TASK_ID");
                verifyingAuthoritySpc = rs.getString("approved_authority");
                verifyingAuthority = rs.getString("approving_authority");
            }
            if (statusid == 10) {
                ps = con.prepareStatement("UPDATE TASK_MASTER SET STATUS_ID=?, NOTE=?, PENDING_AT = ?, PENDING_SPC = ? WHERE TASK_ID=? ");
                ps.setInt(1, statusid);
                ps.setString(2, note);
                ps.setString(3, verifyingAuthority);
                ps.setString(4, verifyingAuthoritySpc);
                ps.setInt(5, taskid);
                ps.execute();
            } else {
                ps = con.prepareStatement("UPDATE HW_INCREMENT_PROPOSAL_MASTER SET APPROVED_AUTHORITY=?, APPROVE_DATE=? WHERE PROPOSAL_ID=? ");
                ps.setString(1, authspc);
                ps.setTimestamp(2, new Timestamp(actionDate.getTime()));
                ps.setInt(3, propmastId);
                ps.execute();

                ps = con.prepareStatement("UPDATE TASK_MASTER SET STATUS_ID=?, NOTE=? WHERE TASK_ID=? ");
                ps.setInt(1, statusid);
                ps.setString(2, note);
                ps.setInt(3, taskid);
                ps.execute();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public String getOfficeName(String offcode) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String offname = "";

        try {

            con = this.dataSource.getConnection();
            ps = con.prepareStatement("SELECT OFF_EN FROM G_OFFICE WHERE OFF_CODE=?");
            ps.setString(1, offcode);
            rs = ps.executeQuery();
            if (rs.next()) {
                offname = rs.getString("OFF_EN");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs,ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return offname;
    }

    @Override
    public void updateOrderInfo(int propmastId, String ordno, String orderDate) {
        Calendar cal = Calendar.getInstance();
        Connection con = null;
        PreparedStatement ps = null;
        Statement st = null;
        ResultSet rs = null;
        String sqlQuery = "SELECT b.pay_commission, b.matrix_level, b.matrix_cell, a.proposal_for_month, a.proposal_for_year, b.emp_id,b.date_of_present_increment from "
                + "	 HW_INCREMENT_PROPOSAL_MASTER a, HW_INCREMENT_PROPOSAL_DETAIL b WHERE a.PROPOSAL_ID=b.PROPOSAL_ID and a.PROPOSAL_ID=" + propmastId;
        try {

            con = this.dataSource.getConnection();
            ps = con.prepareStatement("UPDATE HW_INCREMENT_PROPOSAL_MASTER SET approved_order_no=?, approved_order_date=?, is_sb_updated='N' WHERE PROPOSAL_ID=?");
            ps.setString(1, ordno);
            ps.setTimestamp(2, new Timestamp(new Date(orderDate).getTime()));
            ps.setInt(3, propmastId);
            ps.execute();

            ps = con.prepareStatement("UPDATE EMP_MAST SET PAY_DATE=?, DATE_OF_NINCR=?, pay_commission, matrix_level, matrix_cell WHERE EMP_ID=?");

            st = con.createStatement();
            rs = st.executeQuery(sqlQuery);
            while (rs.next()) {
                cal.set((rs.getInt("proposal_for_year") + 1), rs.getInt("proposal_for_month"), 1);
                Timestamp time = new Timestamp(cal.getTimeInMillis());
                ps.setTimestamp(1, rs.getTimestamp("date_of_present_increment"));
                ps.setTimestamp(2, new Timestamp(time.getTime()));
                ps.setInt(3, rs.getInt("pay_commission"));
                ps.setInt(4, rs.getInt("matrix_level"));
                ps.setInt(5, rs.getInt("matrix_cell"));
                ps.setString(6, rs.getString("emp_id"));
                ps.execute();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs,ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public ProposalAttr getordnoSpcEtc(int proposalMastId) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ProposalAttr pi = new ProposalAttr();
        try {

            con = this.dataSource.getConnection();
            ps = con.prepareStatement("select post,approved_order_no,approved_order_date from ( "
                    + "select gpc,approved_order_no,approved_order_date from ( select approved_order_no,approved_order_date,approved_authority from HW_INCREMENT_PROPOSAL_MASTER where proposal_id=?) HW_INCREMENT_PROPOSAL_MASTER\n"
                    + "left outer join g_spc on HW_INCREMENT_PROPOSAL_MASTER.approved_authority=g_spc.spc) incrprop "
                    + "left outer join g_post on incrprop.gpc=g_post.post_code");

            ps.setInt(1, proposalMastId);
            rs = ps.executeQuery();
            if (rs.next()) {
                pi.setOrdno(rs.getString("approved_order_no"));
                pi.setOrderDate(CommonFunctions.getFormattedOutputDate3(rs.getDate("approved_order_date")));
                pi.setPost(rs.getString("post"));

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs,ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return pi;
    }

    @Override
    public int getProposalMasterId(int taskId) {
        int proposalId = 0;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("SELECT PROPOSAL_ID FROM HW_INCREMENT_PROPOSAL_MASTER WHERE TASK_ID=?");
            ps.setInt(1, taskId);
            rs = ps.executeQuery();
            if (rs.next()) {
                proposalId = rs.getInt("PROPOSAL_ID");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return proposalId;
    }

    @Override
    public int getTaskStatusId(int taskId) {
        int statusId = 0;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("SELECT status_id FROM task_master WHERE TASK_ID=?");
            ps.setInt(1, taskId);
            rs = ps.executeQuery();
            if (rs.next()) {
                statusId = rs.getInt("status_id");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return statusId;
    }

    @Override
    public List getLastYearPay(String empId) {
        List li = new ArrayList();
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        String sql = "";
        try {
            sql = "select TO_DATE(CONCAT((AQ_MONTH+1), '/01/', AQ_YEAR, ' 00:00:00 00'), 'MM/DD/YYYY')::timestamp AS salary_date, gross_amt, aq_month"
                    + ", aq_year, ded_amt, pvt_ded_amt, (gross_amt-ded_amt-COALESCE(pvt_ded_amt, 0)) as net_salary  from aq_mast"
                    + " where TO_DATE(CONCAT((AQ_MONTH+1), '/01/', AQ_YEAR, ' 00:00:00 00'), 'MM/DD/YYYY')::timestamp > date_trunc('month', CURRENT_DATE) - INTERVAL '1 year'"
                    + " and emp_code = '" + empId + "' order by TO_DATE(CONCAT((AQ_MONTH+1), '/01/', AQ_YEAR, ' 00:00:00 00'), 'MM/DD/YYYY')::timestamp";

            //System.out.println(sql);
            con = this.dataSource.getConnection();
            st = con.createStatement();
            rs = st.executeQuery(sql);

            EmpPayBean epb = null;
            while (rs.next()) {
                epb = new EmpPayBean();
                epb.setAqMonth(rs.getString("aq_month"));
                epb.setAqYear(rs.getString("aq_year"));
                epb.setSalaryDate(CommonFunctions.getFormattedOutputDate3(rs.getDate("salary_date")));
                epb.setGrossAmount(rs.getInt("gross_amt") + "");
                epb.setDeduction(rs.getInt("ded_amt") + "");
                epb.setPvtDeduction(rs.getInt("pvt_ded_amt") + "");
                epb.setNetAmount(rs.getInt("net_salary") + "");
                li.add(epb);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return li;
    }

    @Override
    public List getIncrementStatusList() {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        SelectOption so = null;
        List li = new ArrayList();
        try {
            con = this.dataSource.getConnection();
            pst = con.prepareStatement("SELECT STATUS_ID,STATUS_NAME, ACTION_NAME FROM G_PROCESS_STATUS WHERE PROCESS_ID=4 AND status_id IN(11,12)");
            rs = pst.executeQuery();

            while (rs.next()) {
                so = new SelectOption();
                so.setLabel(rs.getString("ACTION_NAME"));
                so.setValue(rs.getString("STATUS_ID"));
                li.add(so);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return li;
    }

    @Override
    public void SaveEmployeeOrder(EmpOrderBean eBean, String spc, String offCode, String deptCode) {
        Calendar cal = Calendar.getInstance();
        Connection con = null;
        PreparedStatement ps = null;
        String orderDate = "";
        String memoNumber = "";
        String orderNumber = "";
        String proposalDetailId = "";
        PreparedStatement ps1 = null;
        PreparedStatement ps2 = null;
        PreparedStatement pst = null;
        PreparedStatement pst1 = null;
        Statement st1 = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        String sqlQuery = "";
        try {
            con = this.dataSource.getConnection();

            String pidArray[] = eBean.getProposalDetailId().split(",");
            String arroNumber[] = eBean.getOrderNumber().split(",");
            String arrmNumber[] = eBean.getMemoNumber().split(",");
            String arroDate[] = eBean.getOrderDate().split(",");
            String empId = null;
            int proposalId = 0;
            Calendar now = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
            String startTime = dateFormat.format(cal.getTime());
            Date actionDate = dateFormat.parse(startTime);
            for (int i = 0; i < pidArray.length; i++) {
                orderNumber = arroNumber[i];
                memoNumber = arrmNumber[i];
                orderDate = arroDate[i];
                proposalDetailId = pidArray[i];
                String sbDescription = "";
                int incrementedBasic = 0;
                int currentBasic = 0;
                int incrementAmount = 0;
                Timestamp ts = null;
                int payCommission = 0;
                String wefDate = "";
                String payScale = "";
                int payLevel = 0;
                int payCell = 0;
                double newPay = 0;
                //System.out.println("Order Number: "+orderNumber+"->"+memoNumber+"->"+orderDate+"->"+proposalDetailId);
                ps = con.prepareStatement("UPDATE HW_INCREMENT_PROPOSAL_DETAIL SET order_number=?, memo_number=?, order_date=?"
                        + " WHERE proposal_detail_id=?");
                ps.setString(1, orderNumber);
                ps.setString(2, memoNumber);
                ps.setTimestamp(3, new Timestamp(new Date(orderDate).getTime()));
                ps.setInt(4, Integer.parseInt(proposalDetailId));
                ps.execute();

                ps1 = con.prepareStatement("UPDATE EMP_MAST SET PAY_DATE=?, DATE_OF_NINCR=?, pay_commission = ?, matrix_level = ?, matrix_cell = ?, cur_basic_salary = ? WHERE EMP_ID = ?");
                sqlQuery = "SELECT PD.*, PM.proposal_id, EM.cur_salary, proposal_for_month, proposal_for_year FROM HW_INCREMENT_PROPOSAL_DETAIL PD"
                        + " INNER JOIN hw_increment_proposal_master PM ON PD.proposal_id = PM.proposal_id"
                        + " INNER JOIN EMP_MAST EM ON PD.emp_id = EM.emp_id"
                        + " WHERE proposal_detail_id = " + proposalDetailId;
                st1 = con.createStatement();
                rs = st1.executeQuery(sqlQuery);
                while (rs.next()) {
                    empId = rs.getString("emp_id");
                    incrementedBasic = rs.getInt("incremented_basic");
                    currentBasic = rs.getInt("current_basic");
                    incrementAmount = rs.getInt("increment_amt");
                    proposalId = rs.getInt("proposal_id");
                    cal.set((rs.getInt("proposal_for_year") + 1), rs.getInt("proposal_for_month"), 1);
                    Timestamp time = new Timestamp(cal.getTimeInMillis());
                    ps1.setTimestamp(1, rs.getTimestamp("date_of_present_increment"));
                    ps1.setTimestamp(2, new Timestamp(time.getTime()));
                    ps1.setInt(3, rs.getInt("pay_commission"));
                    ps1.setInt(4, rs.getInt("matrix_level"));
                    ps1.setInt(5, rs.getInt("matrix_cell"));
                    ps1.setInt(6, rs.getInt("incremented_basic"));
                    ps1.setString(7, rs.getString("emp_id"));
                    ps1.execute();
                    ts = rs.getTimestamp("date_of_present_increment");
                    payCommission = rs.getInt("pay_commission");
                    newPay = rs.getInt("incremented_basic");
                    if (payCommission == 6) {
                        // RS. 10000-325-15200/- WEF 29-APR-2020, FN
                        payScale = " IN THE SCALE OF PAY RS. " + rs.getString("cur_salary") + "/-";

                    }
                    if (payCommission == 7) {
                        payLevel = rs.getInt("matrix_level");
                        payCell = rs.getInt("matrix_cell");
                        payScale = " IN CELL " + rs.getInt("matrix_cell") + " OF LEVEL " + rs.getInt("matrix_level");
                    }
                }
                Users ue = getIncrEmpDetail(empId);

                //DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
                //String startTime = dateFormat.format(cal.getTime());
                //Date initiatedDt = dateFormat.parse(startTime);
                Date entryDate = now.getTime();

                //Now insert into notification table
                sbDescription = " ALLOWED @ RS. " + incrementAmount + "/- RAISING PAY FROM RS. " + currentBasic + "/- TO RS. " + incrementedBasic + "/- PM" + payScale
                        + " WEF " + CommonFunctions.getFormattedOutputDate1(ts) + ", FN VIDE NOTIFICATION/ OFFICE ORDER NO. " + orderNumber + " DATED " + orderDate + ".";
                pst = con.prepareStatement("INSERT INTO EMP_NOTIFICATION(NOT_TYPE,EMP_ID,DOE,ORDNO,ORDDT,DEPT_CODE,OFF_CODE"
                        + ",AUTH,ENT_DEPT,ENT_OFF,ENT_AUTH,IF_VISIBLE,IS_LOCKED, ENTRY_MODE, SB_DESCRIPTION) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
                pst.setString(1, "INCREMENT");
                pst.setString(2, empId);
                pst.setTimestamp(3, new Timestamp(new Date().getTime()));
                pst.setString(4, orderNumber);
                pst.setTimestamp(5, new Timestamp(new Date(orderDate).getTime()));
                pst.setString(6, ue.getDepcode());
                pst.setString(7, ue.getOffcode());
                pst.setString(8, ue.getPostCode());
                pst.setString(9, deptCode);
                pst.setString(10, offCode);
                pst.setString(11, spc);
                pst.setString(12, "Y");
                pst.setString(13, "Y");
                pst.setString(14, "W");
                pst.setString(15, sbDescription);
                pst.executeUpdate();
                rs1 = pst.getGeneratedKeys();
                rs1.next();
                int notificationID = rs1.getInt("not_id");
                //System.out.println("Not ID:"+notificationID);
                //Now insert into emp_increment table
                pst1 = con.prepareStatement("INSERT INTO emp_incr (not_type, emp_id, incr, incr_type, not_id) VALUES(?, ?, ?, ?,?)");
                pst1.setString(1, "INCREMENT");
                pst1.setString(2, empId);
                pst1.setInt(3, incrementAmount);
                pst1.setString(4, "A");
                pst1.setInt(5, notificationID);
                pst1.executeUpdate();

                //Insert into emp_pay_record table
                pst1 = con.prepareStatement("INSERT INTO emp_pay_record(not_id, not_type, emp_id, wef, weft, pay_level, pay_cell, pay) VALUES(?,?,?,?,?,?,?,?)");
                pst1.setInt(1, notificationID);
                pst1.setString(2, "INCREMENT");
                pst1.setString(3, empId);
                pst1.setTimestamp(4, ts);
                pst1.setString(5, "FN");
                pst1.setInt(6, payLevel);
                pst1.setInt(7, payCell);
                pst1.setDouble(8, newPay);
                pst1.executeUpdate();
            }
            ps2 = con.prepareStatement("UPDATE hw_increment_proposal_master SET is_order_updated = 'Y' WHERE proposal_id = ?");
            ps2.setInt(1, proposalId);
            ps2.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs,ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public Users getIncrEmpDetail(String empId) {
        Users empBean = new Users();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = null;
        try {
            con = this.dataSource.getConnection();
            sql = "SELECT ARRAY_TO_STRING(ARRAY[A.INITIALS, A.F_NAME, A.M_NAME, A.L_NAME], ' ') EMPNAME,"
                    + " B.SPC, B.SPN, B.GPC, C.POST,C.POST_CODE, E.off_en, E.off_code"
                    + ",C.DEPARTMENT_CODE,GD.department_name, A.GP FROM EMP_MAST A "
                    + "	LEFT outer join G_SPC B on A.CUR_SPC=B.SPC "
                    + "	left outer join g_post C on B.gpc=C.post_code "
                    + " LEFT OUTER JOIN g_department GD ON C.department_code = GD.department_code, "
                    + "	G_OFFICE E "
                    + "	WHERE A.CUR_OFF_CODE=E.OFF_CODE AND A.EMP_ID=? ";
            ps = con.prepareStatement(sql);
            ps.setString(1, empId);
            rs = ps.executeQuery();
            if (rs.next()) {
                empBean.setFullName(rs.getString("EMPNAME"));
                empBean.setPostname(rs.getString("SPN"));
                empBean.setPostCode(rs.getString("spc"));
                empBean.setDeptName(rs.getString("department_name"));
                empBean.setDepcode(rs.getString("department_code"));
                empBean.setPostname(rs.getString("POST"));
                empBean.setOffname(rs.getString("off_en"));
                empBean.setOffcode(rs.getString("off_code"));
                empBean.setGradepay(Double.parseDouble(rs.getString("gp")));
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
    public List getIncrementPostListWithName(String offcode) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        List spclist = new ArrayList();

        SubstantivePost sp = null;
        String post = "";
        try {
            con = this.dataSource.getConnection();

            String sql = "SELECT POST,SPC,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMPNAME FROM("
                    + " SELECT SPC,POST FROM ("
                    + " SELECT DISTINCT GPC,SPC FROM G_SPC WHERE OFF_CODE=?) G_SPC"
                    + " INNER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE) G_POST"
                    + " INNER JOIN EMP_MAST ON G_POST.SPC=EMP_MAST.CUR_SPC ORDER BY POST";
            pst = con.prepareStatement(sql);
            pst.setString(1, offcode);

            rs = pst.executeQuery();
            while (rs.next()) {
                post = rs.getString("POST");
                if (rs.getString("EMPNAME") != null && !rs.getString("EMPNAME").equals("")) {
                    post = post + " [" + rs.getString("EMPNAME") + "]";
                }
                sp = new SubstantivePost();
                sp.setPostname(post);
                sp.setSpc(rs.getString("SPC"));
                spclist.add(sp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return spclist;
    }

    @Override
    public List getFinalFutureProposedList(String offcode, int year, int month) {
        List li = new ArrayList();
        Connection con = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        PreparedStatement pstmt = null;
        PreparedStatement ps = null;
        String sql = "";
        try {
            sql = " select * from (SELECT PROPOSAL_DETAIL_ID,EMP_ID,GPF_NO, order_number, memo_number, order_date, PAY_COMMISSION,MATRIX_LEVEL, MATRIX_CELL,EMPNAME,CUR_SALARY,CURRENT_BASIC,INCREMENT_AMT "
                    + "  ,INCREMENTED_BASIC,GP,PAY_DATE,DATE_OF_NINCR,F_NAME,POST,date_of_present_increment,(date_of_present_increment-INTERVAL '1 years') as previous_incr_date,date_of_present_pay FROM ( SELECT PROPOSAL_DETAIL_ID "
                    + "  , HW_INCREMENT_PROPOSAL_DETAIL.order_number,HW_INCREMENT_PROPOSAL_DETAIL.memo_number, HW_INCREMENT_PROPOSAL_DETAIL.order_date, PAY_COMMISSION,MATRIX_LEVEL, MATRIX_CELL,EMP_ID,GPF_NO "
                    + "  ,EMPNAME,CURRENT_BASIC,CUR_SALARY,INCREMENT_AMT,INCREMENTED_BASIC,HW_INCREMENT_PROPOSAL_DETAIL.GP,PAY_DATE,DATE_OF_NINCR,F_NAME,CUR_SPC,GPC,date_of_present_increment "
                    + "  ,date_of_present_pay FROM (SELECT EMP_MAST.EMP_ID, HW_INCREMENT_PROPOSAL_DETAIL.order_number, HW_INCREMENT_PROPOSAL_DETAIL.memo_number, HW_INCREMENT_PROPOSAL_DETAIL.order_date, HW_INCREMENT_PROPOSAL_DETAIL.PAY_COMMISSION, HW_INCREMENT_PROPOSAL_DETAIL.MATRIX_LEVEL "
                    + "   , HW_INCREMENT_PROPOSAL_DETAIL.MATRIX_CELL,GPF_NO,CUR_SALARY,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMPNAME "
                    + "   ,F_NAME,CUR_SPC, GP,PAY_DATE,DATE_OF_NINCR,INCREMENTED_BASIC,CURRENT_BASIC,INCREMENT_AMT,PROPOSAL_DETAIL_ID,date_of_present_increment "
                    + "   ,date_of_present_pay FROM (SELECT PROPOSAL_DETAIL_ID,order_number, order_date, memo_number, EMP_ID,CURRENT_BASIC,INCREMENT_AMT,INCREMENTED_BASIC,pay_commission, matrix_level, matrix_cell "
                    + "   , date_of_present_increment,date_of_present_pay FROM HW_INCREMENT_PROPOSAL_DETAIL WHERE EXTRACT(YEAR FROM date_of_present_increment) =2023 and EXTRACT(MONTH FROM date_of_present_increment) =3) HW_INCREMENT_PROPOSAL_DETAIL "
                    + "   INNER JOIN EMP_MAST ON HW_INCREMENT_PROPOSAL_DETAIL.EMP_ID=EMP_MAST.EMP_ID WHERE CUR_OFF_CODE=?) HW_INCREMENT_PROPOSAL_DETAIL  "
                    + "    LEFT OUTER JOIN G_SPC ON HW_INCREMENT_PROPOSAL_DETAIL.CUR_SPC=G_SPC.SPC) HW_INCREMENT_PROPOSAL_DETAIL "
                    + "     LEFT OUTER JOIN G_POST ON HW_INCREMENT_PROPOSAL_DETAIL.GPC=G_POST.POST_CODE )EMPMATRIX left outer join pay_matrix_2017 on   pay_matrix_2017.gp_level=EMPMATRIX.matrix_level "
                    + "	 and pay_matrix_2017.level_slno=(EMPMATRIX.matrix_cell)+1 ORDER BY F_NAME";

            //System.out.println(sql);
            con = this.dataSource.getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, offcode);
            // pstmt.setInt(2, 3);
            rs = pstmt.executeQuery();
            String futurePay = "";
            ProposalAttr pro = null;
            String payScale = "";
            String empId = "";
            while (rs.next()) {
                pro = new ProposalAttr();
                if (rs.getString("PAY_COMMISSION") != null && Integer.parseInt(rs.getString("PAY_COMMISSION")) == 6) {
                    // RS. 10000-325-15200/- WEF 29-APR-2020, FN
                    payScale = "RS. " + rs.getString("cur_salary") + "/-";

                }
                if (rs.getString("PAY_COMMISSION") != null && Integer.parseInt(rs.getString("PAY_COMMISSION")) == 7) {
                    payScale = " IN CELL " + rs.getInt("matrix_cell") + " OF LEVEL " + rs.getInt("matrix_level");
                }
                pro.setProposaldetailId(rs.getInt("PROPOSAL_DETAIL_ID"));
                pro.setEmpId(rs.getString("EMP_ID"));
                pro.setGpfno(rs.getString("GPF_NO"));
                pro.setEmpname(rs.getString("EMPNAME"));
                pro.setNextincr(CommonFunctions.getFormattedOutputDate3(rs.getDate("date_of_present_increment")));
                pro.setPresentpay(rs.getString("CURRENT_BASIC"));
                pro.setPayscale(payScale);
                pro.setGp(rs.getString("GP") + "");
                pro.setPresentpaydate(CommonFunctions.getFormattedOutputDate3(rs.getDate("previous_incr_date")));
                pro.setOrdno(rs.getString("order_number"));
                pro.setOrderDate(CommonFunctions.getFormattedOutputDate3(rs.getDate("order_date")));
                pro.setMemoNo(rs.getString("memo_number"));
                pro.setPost(rs.getString("POST"));
                pro.setPayCommission(rs.getString("PAY_COMMISSION"));
                pro.setMatrixCell(rs.getInt("matrix_cell") + "");
                pro.setMatrixLevel(rs.getInt("matrix_level") + "");
                empId = rs.getString("EMP_ID");
                pro.setFuturepay(rs.getString("amt"));
                pro.setNextMatrixCell(rs.getString("level_slno"));
//                if (rs.getString("PAY_COMMISSION") != null && rs.getString("PAY_COMMISSION").equals("7")) {
//                    if (pro.getMatrixCell() != null && !pro.getMatrixCell().equals("") && pro.getMatrixLevel() != null && !pro.getMatrixLevel().equals("")) {
//                        sql = "SELECT amt FROM pay_matrix_2017 WHERE gp_level = ? AND level_slno = ? ";
//                        ps = con.prepareStatement(sql);
//                        ps.setInt(1, rs.getInt("matrix_level"));
//                        ps.setInt(2, rs.getInt("matrix_cell"));
//                        rs1 = ps.executeQuery();
//                        while (rs1.next()) {
//                            futurePay = rs1.getInt("amt") + "";
//                        }
//
//                    }
//
//                } else {
//                    Users ue = getIncrEmpDetail(empId);
//
//                    futurePay = (Integer.parseInt(rs.getString("CURRENT_BASIC")) + Integer.parseInt(getFutureAmt(Double.parseDouble(rs.getString("CURRENT_BASIC")), ue.getGradepay()))) + "";
//                }
//                if (futurePay.equals("0")) {
//                    futurePay = "TBD";
//                }
//                pro.setFuturepay(futurePay);
                li.add(pro);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return li;
    }

    public List getMonthlyRetirementList(String offcode, int year, int month) {
        List li = new ArrayList();
        Connection con = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        PreparedStatement pstmt = null;
        PreparedStatement pst = null;
        String sql = "";
        ProposalAttr pro = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement(" SELECT EMP_ID,GPF_NO, EMPNAME,DOS,POST FROM(SELECT EMP_ID,GPF_NO, EMPNAME,DOS,GPC FROM(SELECT EMP_ID,GPF_NO,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME, L_NAME], ' ') EMPNAME,DOS,CUR_SPC FROM EMP_MAST WHERE CUR_OFF_CODE=? "
                    + "	 AND EXTRACT(YEAR FROM DOS) =? and EXTRACT(MONTH FROM DOS) =?)EMPMAST INNER JOIN G_SPC ON G_SPC.SPC=EMPMAST.CUR_SPC)TAB INNER JOIN G_POST ON G_POST.POST_CODE=TAB.GPC");
            pst.setString(1, "OLSFIN0010000");
            pst.setInt(2, 2023);
            pst.setInt(3, 3);
            rs = pst.executeQuery();
            while (rs.next()) {
                pro = new ProposalAttr();
                pro.setEmpId(rs.getString("EMP_ID"));
                pro.setGpfno(rs.getString("GPF_NO"));
                pro.setEmpname(rs.getString("EMPNAME"));
                pro.setPost(rs.getString("POST"));
                pro.setDateOfRetirement(CommonFunctions.getFormattedOutputDate3(rs.getDate("DOS")));
                li.add(pro);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return li;
    }

}
