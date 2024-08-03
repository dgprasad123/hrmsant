/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.ltc;

import hrms.SelectOption;
import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.common.GetUserAttribute;
import hrms.common.Message;
import hrms.dao.leaveAccount.ServiceBookLanguageDAO;
import hrms.model.LTC.FamilyMemberBean;
import hrms.model.LTC.LTCBean;
import hrms.model.LTC.sLTCBean;
import hrms.model.login.Users;
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
 * @author Manoj PC
 */
public class LTCDAOImpl implements LTCDAO {

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
    public void saveEmpLTCData(LTCBean ltBean, String empId, String spc) {
        Connection con = null;
        PreparedStatement ps = null;

        Calendar cal = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        String startTime = dateFormat.format(cal.getTime());
        try {

            Date outputDate = dateFormat.parse(startTime);

            con = this.dataSource.getConnection();

            ps = con.prepareStatement("INSERT INTO HW_EMP_LTC (emp_id,application_date,visit_place"
                    + ",date_of_commencement,leave_type,from_date, to_date, no_of_members"
                    + ", place_of_visit, visit_state, visit_district, mode_of_journey, appropriate_distance"
                    + ", cost_by_train, cost_by_road, cost_by_other_means, advance_amount, any_other_information"
                    + ", created_by, created_by_spc, last_updated_on) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, empId);
            ps.setTimestamp(2, new Timestamp(outputDate.getTime()));
            ps.setString(3, ltBean.getVisitPlace());
            ps.setTimestamp(4, new Timestamp(new Date(ltBean.getDateOfCommencement()).getTime()));
            ps.setString(5, ltBean.getLeaveType());
            ps.setTimestamp(6, new Timestamp(new Date(ltBean.getFromDate()).getTime()));
            ps.setTimestamp(7, new Timestamp(new Date(ltBean.getToDate()).getTime()));
            ps.setInt(8, Integer.parseInt(ltBean.getNoofMembers()));
            ps.setString(9, ltBean.getPlaceofVisit());
            ps.setString(10, ltBean.getVisitState());
            ps.setString(11, ltBean.getVisitDistrict());
            ps.setString(12, ltBean.getModeOfJourney());
            ps.setString(13, ltBean.getAppropriateDistance());
            ps.setInt(14, Integer.parseInt(ltBean.getCostByTrain()));
            ps.setInt(15, Integer.parseInt(ltBean.getCostByRoad()));
            ps.setInt(16, Integer.parseInt(ltBean.getCostByOther()));
            ps.setInt(17, Integer.parseInt(ltBean.getAdvanceAmount()));
            ps.setString(18, ltBean.getAnyOtherInfo());
            ps.setString(19, empId);
            ps.setString(20, spc);
            ps.setTimestamp(21, new Timestamp(outputDate.getTime()));
            ps.executeUpdate();
            //System.out.println("the vl of status is======"+status);
            ResultSet rs = ps.getGeneratedKeys();
            rs.next();
            int ltcID = rs.getInt(1);
            String fMemberName = ltBean.getfMemberName();
            fMemberName = fMemberName.replace(",", ",<==>");
            fMemberName = "<==>" + fMemberName;
            String arrMembers[] = fMemberName.split(",");

            String fMemberRelationship = ltBean.getfMemberRelationship();
            fMemberRelationship = fMemberName.replace(",", ",<==>");
            fMemberRelationship = "<==>" + fMemberName;
            String arrRelationships[] = fMemberRelationship.split(",");

            String fMemberMstatus = ltBean.getfMemberMstatus();
            fMemberMstatus = fMemberMstatus.replace(",", ",<==>");
            fMemberMstatus = "<==>" + fMemberMstatus;
            String arrStatus[] = fMemberMstatus.split(",");

            String fMemberDob = ltBean.getfMemberdob();
            fMemberDob = fMemberDob.replace(",", ",<==>");
            fMemberDob = "<==>" + fMemberDob;
            String arrDob[] = fMemberDob.split(",");

            String isStateGovt = ltBean.getIsStateGovt();
            isStateGovt = isStateGovt.replace(",", ",<==>");
            isStateGovt = "<==>" + isStateGovt;
            String arrSGovt[] = isStateGovt.split(",");

            String monthlyIncome = ltBean.getMonthlyIncome();
            monthlyIncome = monthlyIncome.replace(",", ",<==>");
            monthlyIncome = "<==>" + monthlyIncome;
            String arrMIncome[] = monthlyIncome.split(",");

            for (int i = 1; i <= Integer.parseInt(ltBean.getNoofMembers()); i++) {
                fMemberName = arrMembers[i - 1].replace("<==>", "");
                fMemberRelationship = arrRelationships[i - 1].replace("<==>", "");
                fMemberMstatus = arrStatus[i - 1].replace("<==>", "");
                fMemberDob = arrDob[i - 1].replace("<==>", "");
                isStateGovt = arrSGovt[i - 1].replace("<==>", "");
                monthlyIncome = arrMIncome[i - 1].replace("<==>", "");
                if (monthlyIncome.equals("")) {
                    monthlyIncome = "0";
                }
                ps = con.prepareStatement("INSERT INTO hw_emp_ltc_detail(ltc_id, family_member_name, fmember_relationship, fmember_dob, marrital_status, is_govt_employee, monthly_income) VALUES(?,?,?,?,?,?,?)");
                ps.setInt(1, ltcID);
                ps.setString(2, fMemberName);
                ps.setString(3, fMemberRelationship);
                ps.setString(4, fMemberDob);
                ps.setString(5, fMemberMstatus);
                ps.setString(6, isStateGovt);
                ps.setInt(7, Integer.parseInt(monthlyIncome));
                ps.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public int saveBasicInfo(LTCBean ltBean, String empId, String spc) {
        Connection con = null;
        PreparedStatement ps = null;

        Calendar cal = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        String startTime = dateFormat.format(cal.getTime());
        int ltcID = 0;
        try {

            Date outputDate = dateFormat.parse(startTime);

            con = this.dataSource.getConnection();

            ps = con.prepareStatement("INSERT INTO HW_EMP_LTC (emp_id,application_date,visit_place"
                    + ",date_of_commencement,leave_type,from_date, to_date"
                    + ", place_of_visit, visit_state, visit_district, mode_of_journey, appropriate_distance"
                    + ", created_by, created_by_spc, last_updated_on, is_section1) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, empId);
            ps.setTimestamp(2, new Timestamp(outputDate.getTime()));
            ps.setString(3, ltBean.getVisitPlace());
            ps.setTimestamp(4, new Timestamp(new Date(ltBean.getDateOfCommencement()).getTime()));
            ps.setString(5, ltBean.getLeaveType());
            ps.setTimestamp(6, new Timestamp(new Date(ltBean.getFromDate()).getTime()));
            ps.setTimestamp(7, new Timestamp(new Date(ltBean.getToDate()).getTime()));
            ps.setString(8, ltBean.getVisitPlace());
            ps.setString(9, ltBean.getVisitState());
            ps.setString(10, ltBean.getVisitDistrict());
            ps.setString(11, ltBean.getModeOfJourney());
            ps.setString(12, ltBean.getAppropriateDistance());
            ps.setString(13, empId);
            ps.setString(14, spc);
            ps.setTimestamp(15, new Timestamp(outputDate.getTime()));
            ps.setString(16, "Y");
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            rs.next();
            ltcID = rs.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return ltcID;
    }

    @Override
    public void updateBasicInfo(LTCBean ltBean, String ltcId) {
        Connection con = null;
        PreparedStatement ps = null;

        Calendar cal = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        String startTime = dateFormat.format(cal.getTime());
        int ltcID = Integer.parseInt(ltcId);
        try {

            Date outputDate = dateFormat.parse(startTime);

            con = this.dataSource.getConnection();

            ps = con.prepareStatement("UPDATE HW_EMP_LTC SET "
                    + " visit_place = ?"
                    + ",date_of_commencement = ?"
                    + ",leave_type = ?"
                    + ",from_date = ?"
                    + ", to_date = ?"
                    + ", place_of_visit = ?"
                    + ", visit_state = ?"
                    + ", mode_of_journey = ?"
                    + ", appropriate_distance = ?"
                    + ", last_updated_on = ?"
                    + ", is_section1 ='Y' where hw_ltc_id = ?");
            ps.setString(1, ltBean.getVisitPlace());
            ps.setTimestamp(2, new Timestamp(new Date(ltBean.getDateOfCommencement()).getTime()));
            ps.setString(3, ltBean.getLeaveType());
            ps.setTimestamp(4, new Timestamp(new Date(ltBean.getFromDate()).getTime()));
            ps.setTimestamp(5, new Timestamp(new Date(ltBean.getToDate()).getTime()));
            ps.setString(6, ltBean.getVisitPlace());
            ps.setString(7, ltBean.getVisitState());
            ps.setString(8, ltBean.getModeOfJourney());
            ps.setString(9, ltBean.getAppropriateDistance());
            ps.setTimestamp(10, new Timestamp(outputDate.getTime()));
            ps.setInt(11, ltcID);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            rs.next();
            ltcID = rs.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(ps);
            DataBaseFunctions.closeSqlObjects(con);
        }

    }

    @Override
    public void saveFamilyMember(LTCBean ltBean, String empId) {
        Connection con = null;
        PreparedStatement ps = null;

        Calendar cal = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        String startTime = dateFormat.format(cal.getTime());
        try {

            Date outputDate = dateFormat.parse(startTime);

            con = this.dataSource.getConnection();

            int ltcID = Integer.parseInt(ltBean.getLtcId());

            String fMemberName = ltBean.getfMemberName();
            String fMemberRelationship = ltBean.getfMemberRelationship();
            String fMemberMstatus = ltBean.getfMemberMstatus();
            String fMemberDob = ltBean.getfMemberdob();
            String isStateGovt = ltBean.getIsStateGovt();
            String monthlyIncome = ltBean.getMonthlyIncome();
            if (monthlyIncome.equals("")) {
                monthlyIncome = "0";
            }
            ps = con.prepareStatement("INSERT INTO hw_emp_ltc_detail(ltc_id, family_member_name, fmember_relationship, fmember_dob, marrital_status, is_govt_employee, monthly_income) VALUES(?,?,?,?,?,?,?)");
            ps.setInt(1, ltcID);
            ps.setString(2, fMemberName);
            ps.setString(3, fMemberRelationship);
            ps.setString(4, fMemberDob);
            ps.setString(5, fMemberMstatus);
            ps.setString(6, isStateGovt);
            ps.setInt(7, Integer.parseInt(monthlyIncome));
            ps.executeUpdate();
            ps = con.prepareStatement("UPDATE hw_emp_ltc SET is_section2 = 'Y' WHERE hw_ltc_id = " + ltcID);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void saveRDetails(LTCBean ltBean, String empId) {
        Connection con = null;
        PreparedStatement ps = null;

        Calendar cal = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        String startTime = dateFormat.format(cal.getTime());
        try {

            Date outputDate = dateFormat.parse(startTime);

            con = this.dataSource.getConnection();

            ps = con.prepareStatement("UPDATE HW_EMP_LTC SET"
                    + " cost_by_train = ?"
                    + ", cost_by_road = ?"
                    + ", cost_by_other_means = ?"
                    + ", advance_amount = ?"
                    + ", any_other_information = ?"
                    + ", is_section3 = 'Y'"
                    + " WHERE hw_ltc_id = ? AND emp_id = ?");
            ps.setInt(1, Integer.parseInt(ltBean.getCostByTrain()));
            ps.setInt(2, Integer.parseInt(ltBean.getCostByRoad()));
            ps.setInt(3, Integer.parseInt(ltBean.getCostByOther()));
            ps.setInt(4, Integer.parseInt(ltBean.getAdvanceAmount()));
            ps.setString(5, ltBean.getAnyOtherInfo());
            ps.setInt(6, Integer.parseInt(ltBean.getLtcId()));
            ps.setString(7, empId);
            ps.executeUpdate();
            //System.out.println("the vl of status is======"+status);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void saveIssuingAuthority(LTCBean ltBean, String empId) {
        Connection con = null;
        PreparedStatement ps = null;

        Calendar cal = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        String startTime = dateFormat.format(cal.getTime());
        try {

            Date outputDate = dateFormat.parse(startTime);

            con = this.dataSource.getConnection();

            ps = con.prepareStatement("UPDATE HW_EMP_LTC SET"
                    + " issuing_emp_id = ?"
                    + ", issuing_spc = ?"
                    + ", is_section5 = 'Y'"
                    + " WHERE hw_ltc_id = ? AND emp_id = ?");
            ps.setString(1, GetUserAttribute.getEmpId(con, ltBean.getiAuthoritySpc()));
            ps.setString(2, ltBean.getiAuthoritySpc());

            ps.setInt(3, Integer.parseInt(ltBean.getLtcId()));
            ps.setString(4, empId);
            ps.executeUpdate();
            //System.out.println("the vl of status is======"+status);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void saveReportingAuthority(LTCBean ltBean, String empId) {
        Connection con = null;
        PreparedStatement ps = null;

        Calendar cal = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        String startTime = dateFormat.format(cal.getTime());
        try {

            Date outputDate = dateFormat.parse(startTime);

            con = this.dataSource.getConnection();

            ps = con.prepareStatement("UPDATE HW_EMP_LTC SET"
                    + " approving_emp_id = ?"
                    + ", approved_authority = ?"
                    + ", is_section4 = 'Y'"
                    + " WHERE hw_ltc_id = ? AND emp_id = ?");
            ps.setString(1, GetUserAttribute.getEmpId(con, ltBean.getrAuthoritySpc()));
            ps.setString(2, ltBean.getrAuthoritySpc());

            ps.setInt(3, Integer.parseInt(ltBean.getLtcId()));
            ps.setString(4, empId);
            ps.executeUpdate();
            //System.out.println("the vl of status is======"+status);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public List getLeaveTypes() {
        Connection con = null;

        Statement stmt = null;
        ResultSet rs = null;

        List lTypesList = new ArrayList();
        SelectOption so = null;
        try {
            con = dataSource.getConnection();
            String sql = "SELECT DISTINCT tol from g_leave where tol_id <> 'EOL' AND tol_id <> 'HPL'"
                    + " AND tol_id <> 'QL' AND tol_id <> 'ML' AND tol_id <> 'SL' AND tol_id <> 'EIC' AND tol_id <> 'EEL'"
                    + " AND tol_id <> 'LWP' AND tol_id <> 'PL' AND tol_id <> 'HDL' order by tol";
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                so = new SelectOption();
                so.setValue(rs.getString("tol"));
                so.setLabel(rs.getString("tol"));
                lTypesList.add(so);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return lTypesList;
    }

    @Override
    public List getRelations() {
        Connection con = null;

        Statement stmt = null;
        ResultSet rs = null;

        List lTypesList = new ArrayList();
        SelectOption so = null;
        try {
            con = dataSource.getConnection();
            String sql = "SELECT * FROM g_relation";
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                so = new SelectOption();
                so.setValue(rs.getString("relation"));
                so.setLabel(rs.getString("relation"));
                lTypesList.add(so);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return lTypesList;
    }

    @Override
    public List getFamilyMembers(String ltcId) {
        Connection con = null;

        Statement stmt = null;
        ResultSet rs = null;

        List lTypesList = new ArrayList();
        LTCBean lBean = null;
        try {
            con = dataSource.getConnection();
            String sql = "SELECT * FROM hw_emp_ltc_detail WHERE ltc_id = " + ltcId;
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                lBean = new LTCBean();
                lBean.setfMemberName(rs.getString("family_member_name"));;
                lBean.setfMemberRelationship(rs.getString("fmember_relationship"));
                lBean.setfMemberdob(rs.getString("fmember_dob"));
                lBean.setfMemberMstatus(rs.getString("marrital_status"));
                lBean.setIsStateGovt(rs.getString("is_govt_employee"));
                lBean.setMonthlyIncome(rs.getInt("monthly_income") + "");
                lTypesList.add(lBean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return lTypesList;
    }

    @Override
    public List getEmpLTCList(String empId) {
        Connection con = null;

        Statement stmt = null;
        ResultSet rs = null;

        List lTypesList = new ArrayList();
        LTCBean lBean = null;
        try {
            con = dataSource.getConnection();
            String sql = "SELECT * FROM hw_emp_ltc WHERE created_by = '" + empId + "'";
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                lBean = new LTCBean();
                lBean.setPlaceofVisit(rs.getString("place_of_visit"));
                lBean.setApplicationDate(CommonFunctions.getFormattedOutputDate1(rs.getTimestamp("application_date")));
                lBean.setFromDate(CommonFunctions.getFormattedOutputDate1(rs.getTimestamp("from_date")));
                lBean.setToDate(CommonFunctions.getFormattedOutputDate1(rs.getTimestamp("to_date")));
                lBean.setVisitState(rs.getString("visit_state"));
                lBean.setModeOfJourney(rs.getString("mode_of_journey"));
                lBean.setAppropriateDistance(rs.getString("appropriate_distance"));
                lBean.setCostByTrain(rs.getInt("cost_by_train") + "");
                lBean.setCostByRoad(rs.getInt("cost_by_road") + "");
                lBean.setAdvanceAmount(rs.getInt("advance_amount") + "");
                lBean.setLtcId(rs.getInt("hw_ltc_id") + "");
                lBean.setStatus(rs.getString("status"));
                lBean.setOrderDate(CommonFunctions.getFormattedOutputDate1(rs.getTimestamp("approved_order_date")));
                lBean.setOrderNo(rs.getString("approved_order_no"));
                lBean.setTaskStatus(rs.getInt("task_status_id") + "");
                lBean.setVerificationRemarks(rs.getString("verification_remarks"));
                lTypesList.add(lBean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return lTypesList;
    }

    @Override
    public LTCBean getLtcDetail(String ltcId) {
        Connection con = null;

        Statement stmt = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        Statement stmt1 = null;
        LTCBean lBean = null;
        String sql1 = null;
        try {
            con = dataSource.getConnection();
            String sql = "SELECT *, TRUNC(DATE_PART('day', to_date - from_date)+1) AS num_days FROM hw_emp_ltc WHERE hw_ltc_id = " + Integer.parseInt(ltcId);
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                lBean = new LTCBean();
                int numFamilyMembers = 0;
                lBean.setPlaceofVisit(rs.getString("place_of_visit"));
                lBean.setApplicationDate(CommonFunctions.getFormattedOutputDate1(rs.getTimestamp("application_date")));
                lBean.setFromDate(CommonFunctions.getFormattedOutputDate1(rs.getTimestamp("from_date")));
                lBean.setToDate(CommonFunctions.getFormattedOutputDate1(rs.getTimestamp("to_date")));
                lBean.setVisitState(rs.getString("visit_state"));
                lBean.setModeOfJourney(rs.getString("mode_of_journey"));
                lBean.setAppropriateDistance(rs.getString("appropriate_distance"));
                lBean.setCostByTrain(rs.getInt("cost_by_train") + "");
                lBean.setCostByRoad(rs.getInt("cost_by_road") + "");
                lBean.setAdvanceAmount(rs.getInt("advance_amount") + "");
                lBean.setLtcId(rs.getInt("hw_ltc_id") + "");
                lBean.setVisitPlace(rs.getString("visit_place"));
                lBean.setDateOfCommencement(CommonFunctions.getFormattedOutputDate1(rs.getTimestamp("date_of_commencement")));
                lBean.setLeaveType(rs.getString("leave_type"));
                lBean.setCostByOther(rs.getInt("cost_by_other_means") + "");
                lBean.setCostByTrain(rs.getInt("cost_by_train") + "");
                lBean.setCostByRoad(rs.getInt("cost_by_road") + "");
                lBean.setAdvanceAmount(rs.getInt("advance_amount") + "");
                lBean.setAnyOtherInfo(rs.getString("any_other_information"));
                lBean.setiAuthoritySpc(rs.getString("issuing_spc"));
                lBean.setrAuthoritySpc(rs.getString("approved_authority"));
                lBean.setIsSection1(rs.getString("is_section1"));
                lBean.setIsSection2(rs.getString("is_section2"));
                lBean.setIsSection3(rs.getString("is_section3"));
                lBean.setIsSection4(rs.getString("is_section4"));
                lBean.setIsSection5(rs.getString("is_section5"));
                lBean.setStatus(rs.getString("status"));
                lBean.setTaskId(rs.getInt("task_id") + "");
                lBean.setOrderDate(CommonFunctions.getFormattedOutputDate1(rs.getTimestamp("approved_order_date")));
                lBean.setOrderNo(rs.getString("approved_order_no"));
                lBean.setVerificationRemarks(rs.getString("verification_remarks"));
                ArrayList fmList = new ArrayList();
                FamilyMemberBean fmBean = null;
                sql1 = "SELECT * FROM hw_emp_ltc_detail WHERE ltc_id = " + ltcId;
                stmt1 = con.createStatement();
                rs1 = stmt1.executeQuery(sql1);
                while (rs1.next()) {

                    fmBean = new FamilyMemberBean();

                    fmBean.setfMemberName(rs1.getString("family_member_name"));;
                    fmBean.setfMemberRelationship(rs1.getString("fmember_relationship"));
                    fmBean.setfMemberdob(rs1.getString("fmember_dob"));
                    fmBean.setfMemberMstatus(rs1.getString("marrital_status"));
                    fmBean.setIsStateGovt(rs1.getString("is_govt_employee"));
                    fmBean.setMonthlyIncome(rs1.getInt("monthly_income") + "");
                    fmList.add(fmBean);
                    numFamilyMembers++;
                }
                lBean.setFamilyList(fmList);
                Users ue = getEmpDetail(rs.getString("emp_id"));
                lBean.setEmpName(ue.getFullName());
                lBean.setDesignation(ue.getPostname());
                lBean.setDeptName(ue.getDeptName());
                lBean.setNumFamilyMembers(numFamilyMembers + "");
                lBean.setNumDays(rs.getInt("num_days") + "");
                lBean.setOfficeName(ue.getOffname());
                Users ae = getEmpDetail(rs.getString("approving_emp_id"));
                lBean.setApprovingPost(ae.getPostname());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return lBean;
    }

    @Override
    public int getLTCCount(String empId) {
        int total = 0;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = this.dataSource.getConnection();
            //System.out.println("SELECT COALESCE(COUNT(*),0) AS total_rows FROM " + tableName + where);
            ps = con.prepareStatement("SELECT COALESCE(COUNT(*),0) AS total_rows FROM hw_emp_ltc WHERE created_by = '" + empId + "'");
            //System.out.println("SELECT COALESCE(COUNT(*),0) AS total_rows FROM " + tableName + where);
            rs = ps.executeQuery();
            while (rs.next()) {
                total = rs.getInt("total_rows");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return total;
    }

    @Override
    public String getAuthorityName(String spc) {
        Connection con = null;

        Statement stmt = null;
        ResultSet rs = null;
        String authorityName = "";
        try {
            con = dataSource.getConnection();
            String sql = "SELECT * FROM g_spc WHERE spc = '" + spc + "'";
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                authorityName = rs.getString("spn");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return authorityName;
    }

    @Override
    public void submitLTC(int propmastId, String loggedempid, String loggedspc, String authSpc) {

        Connection con = null;
        PreparedStatement pst = null;
        try {
            con = this.dataSource.getConnection();

            Calendar cal = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
            String startTime = dateFormat.format(cal.getTime());
            Date initiatedDt = dateFormat.parse(startTime);

            //int mcode = CommonFunctions.getMaxCode(con, "TASK_MASTER", "TASK_ID");
            pst = con.prepareStatement("INSERT INTO TASK_MASTER(PROCESS_ID, INITIATED_BY, INITIATED_ON"
                    + ", STATUS_ID, PENDING_AT,APPLY_TO,INITIATED_SPC"
                    + ",PENDING_SPC) Values (?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            //pst.setInt(1, mcode);
            pst.setInt(1, 16);
            pst.setString(2, loggedempid);
            pst.setTimestamp(3, new Timestamp(initiatedDt.getTime()));
            pst.setInt(4, 90);
            pst.setString(5, GetUserAttribute.getEmpId(con, authSpc));
            pst.setString(6, GetUserAttribute.getEmpId(con, authSpc));
            pst.setString(7, loggedspc);
            pst.setString(8, authSpc);
            pst.executeUpdate();
            ResultSet rs = pst.getGeneratedKeys();
            rs.next();
            int taskID = rs.getInt("TASK_ID");
            pst = con.prepareStatement("UPDATE HW_EMP_LTC SET TASK_ID=?, status = 'UNDER VERIFICATION', task_status_id = 90 WHERE hw_ltc_id=?");
            pst.setInt(1, taskID);
            pst.setInt(2, propmastId);
            pst.execute();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public int getProposalMasterId(int taskId) {
        int proposalId = 0;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("SELECT hw_ltc_id FROM hw_emp_ltc WHERE TASK_ID=?");
            ps.setInt(1, taskId);
            rs = ps.executeQuery();
            if (rs.next()) {
                proposalId = rs.getInt("hw_ltc_id");
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
    public int getTaskStatus(int taskId) {
        int proposalId = 0;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("SELECT status_id FROM task_master WHERE TASK_ID=?");
            ps.setInt(1, taskId);
            rs = ps.executeQuery();
            if (rs.next()) {
                proposalId = rs.getInt("status_id");
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
    public Users getEmpDetail(String empId) {
        Users empBean = new Users();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = null;
        try {
            con = this.dataSource.getConnection();
            sql = "SELECT ARRAY_TO_STRING(ARRAY[A.INITIALS, A.F_NAME, A.M_NAME, A.L_NAME], ' ') EMPNAME,"
                    + " B.SPC, B.SPN, B.GPC, C.POST,C.POST_CODE, E.off_en"
                    + ",C.DEPARTMENT_CODE,GD.department_name FROM   EMP_MAST A "
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
                empBean.setDeptName(rs.getString("department_name"));
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
    public List getLTCStatus() {
        Connection con = null;

        Statement stmt = null;
        ResultSet rs = null;

        List trainingstatuslist = new ArrayList();
        SelectOption so = null;
        try {
            con = dataSource.getConnection();
            String sql = "SELECT status_id,status_name, action_name from g_process_status where process_id=16 AND status_id <> 85  AND status_id <> 89 AND status_id <> 90 order by status_name";
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                so = new SelectOption();
                so.setValue(rs.getString("status_id"));
                so.setLabel(rs.getString("action_name"));
                trainingstatuslist.add(so);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return trainingstatuslist;
    }

    @Override
    public Message saveLTCAction(int taskId, int ltcStatus, String loginempid, String loginSpc, String forwardSpc, String note) {

        Connection con = null;

        PreparedStatement pst = null;
        PreparedStatement pst1 = null;
        PreparedStatement pst2 = null;
        ResultSet rs2 = null;
        String startTime = "";
        Calendar cal = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
        startTime = dateFormat.format(cal.getTime());
        String pendingAt = "";
        String pendingSpc = "";
        String strStatus = "";
        Message msg = new Message();
        msg.setStatus("Success");

        int logid = 0;

        try {
            con = this.dataSource.getConnection();
            pst2 = con.prepareStatement("SELECT issuing_spc, issuing_emp_id FROM hw_emp_ltc WHERE task_id = ?");
            pst2.setInt(1, taskId);
            rs2 = pst2.executeQuery();
            while (rs2.next()) {
                pendingAt = rs2.getString("issuing_emp_id");
                pendingSpc = rs2.getString("issuing_spc");
            }
            String forwardEmpId = null;
            if (ltcStatus == 86) {
                forwardEmpId = GetUserAttribute.getEmpId(con, forwardSpc);
                pst = con.prepareStatement("UPDATE TASK_MASTER SET PENDING_AT=?,PENDING_SPC=?,STATUS_ID=? WHERE TASK_ID=?");
                pst.setString(1, forwardEmpId);
                pst.setString(2, forwardSpc);
                pst.setInt(3, ltcStatus);
                pst.setInt(4, taskId);
                pst.executeUpdate();
                pst = con.prepareStatement("INSERT INTO WORKFLOW_LOG(LOG_ID,REF_ID,ACTION_TAKEN_BY,FORWARDED_SPC,FORWARD_TO,NOTE,SPC_ONTIME,TASK_ACTION_DATE,TASK_ID,TASK_STATUS_ID,WORKFLOW_TYPE,AUTHORITY_TYPE) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)");
                logid = CommonFunctions.getMaxCodeInteger("WORKFLOW_LOG", "LOG_ID", con);
                pst.setInt(1, logid);
                pst.setInt(2, 0);
                pst.setString(3, loginempid);
                pst.setString(4, forwardSpc);
                pst.setString(5, forwardEmpId);
                pst.setString(6, "");
                pst.setString(7, "");
                pst.setTimestamp(8, new Timestamp(dateFormat.parse(startTime).getTime()));
                pst.setInt(9, taskId);
                pst.setInt(10, ltcStatus);
                pst.setString(11, "LTC_FORWARD");
                pst.setString(12, "");
                pst.executeUpdate();
                strStatus = "FORWARDED";
            } else if (ltcStatus == 87) {
                pst1 = con.prepareStatement("UPDATE task_master SET status_id=?, pending_at = ?"
                        + ", pending_spc = ?"
                        + " WHERE TASK_ID=?");
                pst1.setInt(1, ltcStatus);
                pst1.setString(2, pendingAt);
                pst1.setString(3, pendingSpc);
                pst1.setInt(4, taskId);
                pst1.executeUpdate();
                strStatus = "APPROVED";
            } else if (ltcStatus == 88) {
                pst1 = con.prepareStatement("UPDATE task_master SET status_id=?, note = ?"
                        + " WHERE TASK_ID=?");
                pst1.setInt(1, ltcStatus);
                pst1.setString(2, note);
                pst1.setInt(3, taskId);
                pst1.executeUpdate();
                strStatus = "DECLINED";
            }
            pst1 = con.prepareStatement("UPDATE hw_emp_ltc SET status=?, approved_authority = ?"
                    + ", approving_emp_id = ?"
                    + ", approve_date = ?, task_status_id = ? WHERE TASK_ID=?");
            pst1.setString(1, strStatus);
            pst1.setString(2, loginSpc);
            pst1.setString(3, loginempid);
            pst1.setTimestamp(4, new Timestamp(dateFormat.parse(startTime).getTime()));
            pst1.setInt(5, ltcStatus);
            pst1.setInt(6, taskId);
            pst1.executeUpdate();

        } catch (Exception e) {
            msg.setStatus("Error");
            msg.setMessage(e.getMessage());
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return msg;
    }

    @Override
    public Message saveOrderInfo(int taskId, String orderDate, String orderNo, int ltcId) {
        Message msg = new Message();
        msg.setStatus("Success");
        Connection con = null;
        PreparedStatement ps = null;
        Statement st = null;
        ResultSet rs = null;
        try {

            con = this.dataSource.getConnection();
            ps = con.prepareStatement("UPDATE HW_EMP_LTC SET approved_order_no=?, approved_order_date=?, status = 'COMPLETED', task_status_id = 89 WHERE hw_ltc_id=?");
            ps.setString(1, orderNo);
            ps.setTimestamp(2, new Timestamp(new Date(orderDate).getTime()));
            ps.setInt(3, ltcId);
            ps.executeUpdate();

            ps = con.prepareStatement("UPDATE TASK_MASTER SET status_id = ? WHERE task_id = ?");
            ps.setInt(1, 89);
            ps.setInt(2, taskId);
            ps.executeUpdate();

        } catch (Exception e) {
            msg.setStatus("Error");
            msg.setMessage(e.getMessage());
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return msg;
    }

    @Override
    public Message saveVerification(int taskId, String loginempid, String loginSpc, LTCBean lBean) {

        Connection con = null;

        PreparedStatement pst = null;
        PreparedStatement pst1 = null;
        PreparedStatement pst2 = null;
        ResultSet rs2 = null;
        String startTime = "";
        Calendar cal = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
        startTime = dateFormat.format(cal.getTime());
        String pendingAt = "";
        String pendingSpc = "";
        String strStatus = "";
        Message msg = new Message();
        msg.setStatus("Success");
        int taskStatusId = 0;

        try {
            con = this.dataSource.getConnection();
            pst2 = con.prepareStatement("SELECT approved_authority, approving_emp_id FROM hw_emp_ltc WHERE task_id = ?");
            pst2.setInt(1, taskId);
            rs2 = pst2.executeQuery();
            while (rs2.next()) {
                pendingAt = rs2.getString("approving_emp_id");
                pendingSpc = rs2.getString("approved_authority");
            }

            if (lBean.getHasLtcEligibility().equals("Y")) {
                strStatus = "SUBMITTED FOR APPROVAL";
                pst1 = con.prepareStatement("UPDATE task_master SET status_id=?, pending_at = ?"
                        + ", pending_spc = ?"
                        + " WHERE TASK_ID=?");
                pst1.setInt(1, 85);
                pst1.setString(2, pendingAt);
                pst1.setString(3, pendingSpc);
                pst1.setInt(4, taskId);
                pst1.executeUpdate();
                taskStatusId = 85;
            } else {
                strStatus = "NOT ELIGIBLE";
                pst1 = con.prepareStatement("UPDATE task_master SET status_id=?, pending_at = '', pending_spc = ''"
                        + " WHERE TASK_ID=?");
                pst1.setInt(1, 89);
                pst1.setInt(2, taskId);
                pst1.executeUpdate();
                taskStatusId = 89;
            }

            pst1 = con.prepareStatement("UPDATE hw_emp_ltc SET status=?, is_eligible =?, verification_remarks = ?, task_status_id = ?"
                    + " WHERE TASK_ID=?");
            pst1.setString(1, strStatus);
            pst1.setString(2, lBean.getHasLtcEligibility());
            pst1.setString(3, lBean.getRemarks());
            pst1.setInt(4, taskStatusId);
            pst1.setInt(5, taskId);
            pst1.executeUpdate();

        } catch (Exception e) {
            msg.setStatus("Error");
            msg.setMessage(e.getMessage());
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return msg;
    }

    public void saveLTCEntry(String notId, sLTCBean slBean, String deptCode, String offCode, String spc) {
        Connection con = null;
        String sbDescription = null;
        PreparedStatement pst = null;
        PreparedStatement pst1 = null;
        ResultSet rs1 = null;
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");

        String startTime = dateFormat.format(cal.getTime());

        try {
            con = this.dataSource.getConnection();

            Date actionDate = dateFormat.parse(startTime);

            if (slBean.getRadpostingauthtype() != null && !slBean.getRadpostingauthtype().equals("")) {
                if (slBean.getRadpostingauthtype().equals("GOO")) {
                    slBean.setHidNotiSpc(slBean.getHidNotiSpc());
                } else if (slBean.getRadpostingauthtype().equals("GOI")) {
                    slBean.setHidNotiSpc(slBean.getHidOthSpc());
                    slBean.setHidPostedOffCode(getOtherSpn(slBean.getHidOthSpc()));
                }
            }

            sbDescription = sbDAO.generateLTCLanguage(slBean);

            pst = con.prepareStatement("INSERT INTO EMP_NOTIFICATION(NOT_TYPE,EMP_ID,DOE,ORDNO,ORDDT,DEPT_CODE,OFF_CODE,AUTH,ENT_DEPT,ENT_OFF,ENT_AUTH,IF_VISIBLE, SB_DESCRIPTION, NOTE,organization_type) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, "LTC");
            pst.setString(2, slBean.getEmpid());
            pst.setTimestamp(3, new Timestamp(actionDate.getTime()));
            pst.setString(4, slBean.getTxtNotOrdNo());
            pst.setTimestamp(5, new Timestamp(new Date(slBean.getTxtNotOrdDt()).getTime()));
            pst.setString(6, slBean.getHidNotifyingDeptCode());
            pst.setString(7, slBean.getHidNotifyingOffCode());
            pst.setString(8, slBean.getHidNotiSpc());
            pst.setString(9, deptCode);
            pst.setString(10, offCode);
            pst.setString(11, spc);
            if (slBean.getChkNotSBPrint() != null && slBean.getChkNotSBPrint().equals("Y")) {
                pst.setString(12, "N");
            } else {
                pst.setString(12, "Y");
            }
            pst.setString(13, sbDescription);
            pst.setString(14, slBean.getNote());
            pst.setString(15, slBean.getRadpostingauthtype());
            pst.executeUpdate();
            rs1 = pst.getGeneratedKeys();
            rs1.next();
            int notificationID = rs1.getInt("not_id");

            //Now insert into emp_ltc table
            pst1 = con.prepareStatement("INSERT INTO emp_ltc (emp_id, not_id, not_type, home_town, fdate, tdate, fbl_year, tbl_year) VALUES(?,?,?,?,?,?,?,?)");
            pst1.setString(1, slBean.getEmpid());
            pst1.setInt(2, notificationID);
            pst1.setString(3, "LTC");
            pst1.setString(4, slBean.getChkHomeTown());
            pst1.setTimestamp(5, new Timestamp(new Date(slBean.getFromDate()).getTime()));
            pst1.setTimestamp(6, new Timestamp(new Date(slBean.getToDate()).getTime()));
            pst1.setInt(7, Integer.parseInt(slBean.getFblYear()));
            pst1.setInt(8, Integer.parseInt(slBean.getTblYear()));
            pst1.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void updateLTCEntry(sLTCBean slBean) {
        Connection con = null;
        String sbDescription = null;
        PreparedStatement pst = null;
        PreparedStatement pst1 = null;
        ResultSet rs1 = null;
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");

        String startTime = dateFormat.format(cal.getTime());

        try {
            con = this.dataSource.getConnection();

            Date actionDate = dateFormat.parse(startTime);

            if (slBean.getRadpostingauthtype() != null && !slBean.getRadpostingauthtype().equals("")) {
                if (slBean.getRadpostingauthtype().equals("GOO")) {
                    slBean.setHidNotiSpc(slBean.getHidNotiSpc());
                } else if (slBean.getRadpostingauthtype().equals("GOI")) {
                    slBean.setHidNotiSpc(slBean.getHidOthSpc());
                    slBean.setHidPostedOffCode(getOtherSpn(slBean.getHidOthSpc()));
                }
            }
            System.out.println("HidPostedOffCode is: " + slBean.getHidPostedOffCode());
            sbDescription = sbDAO.generateLTCLanguage(slBean);

            pst = con.prepareStatement("UPDATE EMP_NOTIFICATION SET ORDNO = ?,ORDDT = ?"
                    + ",DEPT_CODE =?,OFF_CODE =?,AUTH = ?"
                    + ",SB_DESCRIPTION = ?, NOTE =?,organization_type=?,if_visible=? WHERE not_id = ?");
            pst.setString(1, slBean.getTxtNotOrdNo());
            pst.setTimestamp(2, new Timestamp(new Date(slBean.getTxtNotOrdDt()).getTime()));
            pst.setString(3, slBean.getHidNotifyingDeptCode());
            pst.setString(4, slBean.getHidNotifyingOffCode());
            pst.setString(5, slBean.getHidNotiSpc());
            pst.setString(6, sbDescription);
            pst.setString(7, slBean.getNote());
            pst.setString(8, slBean.getRadpostingauthtype());
            if (slBean.getChkNotSBPrint() != null && slBean.getChkNotSBPrint().equals("Y")) {
                pst.setString(9, "N");

            } else {
                pst.setString(9, "Y");
            }
            pst.setInt(10, slBean.getHidNotId());
            pst.executeUpdate();

            //Now insert into emp_ltc table
            pst1 = con.prepareStatement("UPDATE emp_ltc SET home_town = ?, fdate = ?, tdate = ?, fbl_year = ?, tbl_year =? WHERE ltc_id = ?");
            pst1.setString(1, slBean.getChkHomeTown());
            pst1.setTimestamp(2, new Timestamp(new Date(slBean.getFromDate()).getTime()));
            pst1.setTimestamp(3, new Timestamp(new Date(slBean.getToDate()).getTime()));
            pst1.setInt(4, Integer.parseInt(slBean.getFblYear()));
            pst1.setInt(5, Integer.parseInt(slBean.getTblYear()));
            pst1.setInt(6, Integer.parseInt(slBean.getLtcId()));
            pst1.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    public List getLTCEntryList(String empId) {
        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        List ltclist = new ArrayList();
        sLTCBean slBean = null;

        try {
            con = dataSource.getConnection();

            String sql = "select emp_notification.not_id, emp_notification.ordno, emp_notification.orddt, emp_notification.doe, emp_ltc.* from emp_notification"
                    + " inner join emp_ltc on emp_notification.not_id=emp_ltc.not_id  where emp_notification.emp_id=? and emp_notification.not_type=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, empId);
            pst.setString(2, "LTC");
            rs = pst.executeQuery();
            while (rs.next()) {
                slBean = new sLTCBean();
                slBean.setTxtNotOrdNo(rs.getString("ordno"));
                slBean.setHidNotId(rs.getInt("not_id"));
                slBean.setTxtNotOrdDt(CommonFunctions.getFormattedOutputDate1(rs.getTimestamp("orddt")));
                slBean.setDateofEntry(CommonFunctions.getFormattedOutputDate1(rs.getTimestamp("doe")));
                slBean.setChkHomeTown(rs.getString("home_town"));
                slBean.setFblYear(rs.getString("fbl_year") + "");
                slBean.setTblYear(rs.getString("tbl_year") + "");
                slBean.setFromDate(CommonFunctions.getFormattedOutputDate1(rs.getTimestamp("fdate")));
                slBean.setToDate(CommonFunctions.getFormattedOutputDate1(rs.getTimestamp("tdate")));
                ltclist.add(slBean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return ltclist;
    }

    public String getOtherSpn(String othSpc) {
        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;
        String spn = "";
        try {
            con = this.dataSource.getConnection();
            if (othSpc != null && !othSpc.equals("")) {
                pst = con.prepareStatement("SELECT other_spc_id, off_en FROM g_oth_spc WHERE other_spc_id=? and is_active='Y'");
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
    public sLTCBean getSLTCDetail(String empId, int notId) {
        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        sLTCBean slBean = new sLTCBean();
        try {
            con = this.dataSource.getConnection();
            String sql = "select emp_notification.*, emp_ltc.*, spn from emp_notification"
                    + " inner join emp_ltc on emp_notification.not_id=emp_ltc.not_id"
                    + " left outer join g_spc spc1 on emp_notification.auth=spc1.spc"
                    + " where emp_notification.emp_id=? and emp_notification.not_id=?";

            pst = con.prepareStatement(sql);
            pst.setString(1, empId);
            pst.setInt(2, notId);
            rs = pst.executeQuery();
            if (rs.next()) {
                slBean.setHidNotId(rs.getInt("not_id"));
                slBean.setHidNotifyingDeptCode(rs.getString("dept_code"));
                slBean.setHidNotifyingOffCode(rs.getString("off_code"));
                slBean.setHidNotiSpc(rs.getString("auth"));
                slBean.setNotifyingSpc(rs.getString("spn"));
                if (rs.getString("organization_type") != null && !rs.getString("organization_type").equals("GOO")) {
                    slBean.setHidOthSpc(rs.getString("auth"));
                    slBean.setNotifyingSpc(getOtherSpn(rs.getString("auth")));
                } else {
                    slBean.setHidNotiSpc(rs.getString("auth"));
                    slBean.setNotifyingSpc(rs.getString("spn"));
                }
                slBean.setTxtNotOrdNo(rs.getString("ordno"));
                slBean.setTxtNotOrdDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("orddt")));
                slBean.setNote(rs.getString("note"));

                slBean.setLtcId(rs.getString("ltc_id"));
                slBean.setChkHomeTown(rs.getString("home_town"));
                slBean.setFblYear(rs.getString("fbl_year") + "");
                slBean.setTblYear(rs.getString("tbl_year") + "");
                slBean.setFromDate(CommonFunctions.getFormattedOutputDate1(rs.getTimestamp("fdate")));
                slBean.setToDate(CommonFunctions.getFormattedOutputDate1(rs.getTimestamp("tdate")));
                slBean.setRadpostingauthtype(rs.getString("organization_type"));
                if (rs.getString("if_visible").equals("N")) {
                    slBean.setChkNotSBPrint("Y");
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return slBean;
    }

}
