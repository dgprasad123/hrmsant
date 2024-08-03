/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.leave;

import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.model.leave.CreditLeaveBean;
import hrms.model.leave.CreditLeaveProperties;
import hrms.model.leave.EmpLeaveAccountPropeties;
import hrms.model.leave.EmpLeaveAvailedPropeties;
import hrms.model.leave.EmpLeaveSurrenderedPropeties;
import hrms.model.leave.Leave;
import hrms.model.leave.LeaveOpeningBalanceBean;
import hrms.model.leave.LeaveRule;
import hrms.model.leave.Range;
import hrms.model.master.LeaveTypeBean;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.annotation.Resource;
import javax.sql.DataSource;
import org.apache.commons.lang.time.DateUtils;

/**
 *
 * @author Surendra
 */
public class LeaveDAOImpl implements LeaveDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    private EmpLeaveAvailedPropeties leftoutempLeaveAvailedProperties;

    @Override
    public int addLeaveData(Leave leave) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void updateLeaveData(CreditLeaveBean leave) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        //CreditLeaveBean leave=null;
        Calendar cal = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");

        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("update emp_leave_cr set sp_from=?,sp_to=?,com_mon=?,cr_count=? where lcr_id=?");
            if (leave.getFromdate() != null && !leave.getFromdate().equals("")) {
                ps.setTimestamp(1, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(leave.getFromdate()).getTime()));
            } else {
                ps.setTimestamp(1, null);
            }
            if (leave.getTodate() != null && !leave.getTodate().equals("")) {
                ps.setTimestamp(2, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(leave.getTodate()).getTime()));
            } else {
                ps.setTimestamp(2, null);
            }
            ps.setInt(3, Integer.parseInt(leave.getCompMonths()));
            ps.setDouble(4, Double.parseDouble(leave.getCreleave()));
            ps.setString(5, leave.getCreId());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
            DataBaseFunctions.closeSqlObjects(ps);
        }
    }

    @Override
    public int deleteLeaveData(int leaveId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public CreditLeaveBean editLeaveData(String lcrId) {
        //To change body of generated methods, choose Tools | Templates.
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        CreditLeaveBean leave = null;
        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("select tol_id,sp_from,sp_to,com_mon,cr_count from emp_leave_cr where lcr_id=?");
            ps.setString(1, lcrId);
            rs = ps.executeQuery();
            if (rs.next()) {
                leave = new CreditLeaveBean();
                leave.setCreId(lcrId);
                leave.setTolid(rs.getString("tol_id"));
                leave.setFromdate(CommonFunctions.getFormattedOutputDate1(rs.getDate("sp_from")));
                leave.setTodate(CommonFunctions.getFormattedOutputDate1(rs.getDate("sp_to")));
                leave.setCompMonths(rs.getString("com_mon"));
                leave.setCreleave(rs.getString("cr_count"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(ps, con);
        }
        return leave;
    }

    @Override
    public List getAbsenteeList(String empId, String leaveType) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public CreditLeaveProperties getLeaveOBDate(String empId, String tolid) {
        CreditLeaveProperties clp = new CreditLeaveProperties();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("SELECT OB,OB_DATE,OB_TIME,OB_DATE + interval '1' day AS OB_DATE_NXT FROM EMP_LEAVE_OB WHERE EMP_ID=? AND TOL_ID=?");
            ps.setString(1, empId);
            ps.setString(2, tolid);
            rs = ps.executeQuery();
            if (rs.next()) {
                if (rs.getString("OB_DATE") != null && !rs.getString("OB_DATE").equals("")) {
                    if (rs.getString("OB_TIME") != null && !rs.getString("OB_TIME").equals("")) {
                        clp.setFnan(rs.getString("OB_TIME"));
                        clp.setOpenbaldate(CommonFunctions.getFormattedOutputDate1(rs.getDate("OB_DATE")));
                        clp.setNextopenbaldate(CommonFunctions.getFormattedOutputDate1(rs.getDate("OB_DATE_NXT")));
                        clp.setLeaveCredited(rs.getString("OB"));
                    } else {
                        clp.setOpenbaldate(CommonFunctions.getFormattedOutputDate1(rs.getDate("OB_DATE")));
                        clp.setLeaveCredited(rs.getString("OB"));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return clp;
    }

    @Override
    public void updateLeaveCreditData(CreditLeaveProperties clp) {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = this.dataSource.getConnection();
            pstmt = con.prepareStatement("INSERT INTO EMP_LEAVE_CR(LCR_ID, EMP_ID, TOL_ID, SP_FROM, SP_TO, COM_MON, CR_COUNT, CR_TYPE) values (?,?,?,?,?,?,?,?)");
            pstmt.setString(1, CommonFunctions.getMaxCode("EMP_LEAVE_CR", "LCR_ID", con));
            pstmt.setString(2, clp.getEmpId());
            pstmt.setString(3, clp.getTypeoflv());
            pstmt.setDate(4, new java.sql.Date(CommonFunctions.getDateFromString(clp.getFromDate(), "dd-MMM-yyyy").getTime()));
            pstmt.setDate(5, new java.sql.Date(CommonFunctions.getDateFromString(clp.getToDate(), "dd-MMM-yyyy").getTime()));
            pstmt.setDouble(6, Double.parseDouble(clp.getCompMonths()));
            pstmt.setDouble(7, clp.getCreleave());
            pstmt.setString(8, "G");
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pstmt, con);
        }
    }

    @Override
    public ArrayList getUpdateData(String empId, String tolid) {
        ArrayList al = new ArrayList();
        ArrayList alCrList = new ArrayList();
        CreditLeaveProperties clv = getLeaveOBDate(empId, tolid);
        String maxCrDt = getMaxCrDate(empId, tolid);
        String obDate = clv.getOpenbaldate();
        String obNextDate = clv.getNextopenbaldate();
        String saDate = getSADate(empId);
        String startForcastingDate = "";
        String endForcastingDate = "";

        if (obDate != null && !obDate.equals("")) {
            if (clv.getFnan() != null && !clv.getFnan().equals("")) {
                if (clv.getFnan().equals("FN")) {
                    startForcastingDate = obDate;
                } else {
                    startForcastingDate = obNextDate;
                }
            }
        } else {
            al = null;
        }
        if (saDate != null && !saDate.equals("")) {

            endForcastingDate = saDate;
        }
        if (startForcastingDate != null && !startForcastingDate.equals("") && endForcastingDate != null && !endForcastingDate.equals("")) {
            alCrList = getCreditRangeList(empId, tolid, startForcastingDate, endForcastingDate);
            CreditLeaveProperties cleave = null;
            for (int i = 0; i < alCrList.size(); i++) {
                Range range = new Range();
                range = (Range) alCrList.get(i);
                cleave = new CreditLeaveProperties();

                range.setToDate(getEndDate(range.getToDate(), endForcastingDate));
                if (!ifExists(empId, tolid, range.getFromDt(), range.getToDate())) {

                    cleave.setFromDate(range.getFromDt());
                    cleave.setToDate(range.getToDate());
                    cleave.setCompMonths(range.getCompleteMon());
                    cleave.setCreleave(Double.parseDouble(range.getNoofLeave()));
                    cleave.setTypeoflv(tolid);
                } else {
                    CreditLeaveProperties ncl = getCreditLeaveRange(empId, tolid, range.getFromDt(), range.getToDate());
                    cleave.setFromDate(range.getFromDt());
                    cleave.setToDate(range.getToDate());
                    cleave.setCompMonths(ncl.getCompMonths());
                    cleave.setCreleave(ncl.getCreleave());
                    cleave.setLcrId(ncl.getLcrId());
                    cleave.setTypeoflv(tolid);
                }
                al.add(cleave);
            }
        }
        return al;
    }

    public CreditLeaveProperties getCreditLeaveRange(String empid, String tolid, String fdate, String tdate) {
        CreditLeaveProperties cl = new CreditLeaveProperties();
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT * FROM EMP_LEAVE_CR WHERE EMP_ID='" + empid + "' and TOL_ID='" + tolid + "' and SP_FROM='" + fdate + "' and SP_TO='" + tdate + "' and CR_TYPE='G'");
            rs = pstmt.executeQuery();
            while (rs.next()) {
                cl.setEmpId(empid);
                cl.setTypeoflv(tolid);
                cl.setFromDate(fdate);
                cl.setToDate(tdate);
                cl.setCompMonths(rs.getString("COM_MON"));
                cl.setCreleave(rs.getDouble("CR_COUNT"));
                cl.setLcrId(rs.getString("LCR_ID"));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt, con);
        }

        return cl;
    }

    public boolean ifExists(String empId, String tolid, String fdate, String tdate) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        boolean flag = false;

        try {
            con = dataSource.getConnection();
            if (tdate != null && !tdate.equals("")) {
                pstmt = con.prepareStatement("SELECT count(*) countrc FROM EMP_LEAVE_CR WHERE EMP_ID=? and TOL_ID=? and SP_FROM='" + fdate + "' and SP_TO='" + tdate + "' AND CR_TYPE='G'");
                pstmt.setString(1, empId);
                pstmt.setString(2, tolid);
                rs = pstmt.executeQuery();
                while (rs.next()) {
                    int cnt = rs.getInt("countrc");
                    if (cnt > 0) {
                        flag = true;
                    }

                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt, con);
        }

        return flag;
    }

    public String getEndDate(String toDate, String endDate) {
        String finalEndDate = null;
        Date toDateObj = null;
        Date endDateObj = null;
        try {
            DateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
            toDateObj = df.parse(toDate);
            endDateObj = df.parse(endDate);
            if (toDateObj.after(endDateObj)) {
                finalEndDate = CommonFunctions.getFormattedOutputDate1(endDateObj);
            } else {
                finalEndDate = CommonFunctions.getFormattedOutputDate1(toDateObj);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return finalEndDate;
    }

    public String getMaxCrDate(String empId, String tolid) {
        String maxCrDt = "";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("SELECT MAX(SP_TO) SP_TO FROM EMP_LEAVE_CR WHERE EMP_ID=? AND TOL_ID=?");
            ps.setString(1, empId);
            ps.setString(2, tolid);
            rs = ps.executeQuery();
            while (rs.next()) {
                if (rs.getString("SP_TO") != null && !rs.getString("SP_TO").equals("")) {
                    maxCrDt = rs.getString("SP_TO");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }

        return maxCrDt;
    }

    public String getSADate(String empId) {
        String saDate = "";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("SELECT TO_CHAR(DOS,'dd-MON-yyyy') as DOS FROM EMP_MAST WHERE EMP_ID=?");
            ps.setString(1, empId);
            rs = ps.executeQuery();
            while (rs.next()) {
                if (rs.getString("DOS") != null && !rs.getString("DOS").equals("")) {
                    saDate = rs.getString("DOS");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return saDate;
    }

    public String getNextApplicationFromDate(String fromDate, String empid) {
        String nextFromDate = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = this.dataSource.getConnection();

            ps = con.prepareStatement("SELECT MIN(JOINEDSPC.JOIN_DATE) JOINDATE FROM"
                    + " (SELECT JOIN_DATE,GPC FROM"
                    + " (SELECT JOIN_DATE,SPC FROM EMP_JOIN WHERE EMP_ID='" + empid + "') EMPJOIN "
                    + " INNER JOIN "
                    + " (SELECT SPC,SPN,GPC FROM G_SPC WHERE IFUCLEAN!='Y' OR IFUCLEAN IS NULL) G_SPC ON EMPJOIN.SPC = G_SPC.SPC) JOINEDSPC"
                    + " INNER JOIN G_POST ON JOINEDSPC.GPC = G_POST.POST_CODE AND JOINEDSPC.JOIN_DATE>'" + fromDate + "'");
            rs = ps.executeQuery();
            while (rs.next()) {
                if (rs.getDate("JOINDATE") != null) {
                    nextFromDate = CommonFunctions.getFormattedOutputDate1(rs.getDate("JOINDATE"));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return nextFromDate;
    }

    public ArrayList getCreditRangeList(String empid, String tolid, String startForcastingDate, String endForcastingDate) {
        ArrayList al = new ArrayList();
        ArrayList alFinal = new ArrayList();
        LeaveRule lvTo = null;
        Date startDate = CommonFunctions.getDateFromString(startForcastingDate, "dd-MMM-yyyy");
        Date fromDate = startDate;
        Date endDate = CommonFunctions.getDateFromString(endForcastingDate, "dd-MMM-yyyy");

        while ((fromDate != null && !fromDate.equals("")) && fromDate.before(endDate)) {
            ArrayList tempList = null;
            Range range = new Range();

            range.setFromDt(CommonFunctions.getFormattedOutputDate1(fromDate));
            String frmDt = range.getFromDt();
            LeaveRule lvFrom = getLeaveRule(empid, tolid, frmDt);
            while ((lvFrom == null || lvFrom.getLeaveId().equals("")) && frmDt != null) {

                if (lvFrom.getLeaveId() == null || lvFrom.getLeaveId().equals("")) {
                    frmDt = getNextApplicationFromDate(frmDt, empid);
                    if (frmDt != null && !frmDt.equals("")) {
                        lvFrom = getLeaveRule(empid, tolid, frmDt);
                    }
                    range.setFromDt(frmDt);
                }
            }
            if (lvFrom.getLeaveId() != null && !lvFrom.getLeaveId().equals("")) {
                range.setToDate(getToDate(range.getFromDt(), lvFrom.getInterval(), lvFrom.getAnnualRound()));
                if (range.getToDate() != null && !range.getToDate().equals("")) {
                    lvTo = getLeaveRule(empid, tolid, range.getToDate());
                }
                if (!lvFrom.getLeaveId().equals(lvTo.getLeaveId())) {

                    tempList = getWefWiseInnerRange(empid, tolid, range.getFromDt(), range.getToDate());

                }
                fromDate = getNxtFromDate(range.getToDate());
                if (tempList != null && tempList.size() != 0) {
                    for (int i = 0; i < tempList.size(); i++) {
                        Range rg = new Range();
                        rg = (Range) tempList.get(i);
                        al.add(rg);
                    }
                } else {
                    LeaveRule lvRuleOnDate = null;
                    int compltMon = 0;
                    float creditCnt = 0;
                    float partCredit = 0;
                    float interval = 0;

                    lvRuleOnDate = getLeaveRule(empid, tolid, range.getFromDt());
                    compltMon = getCompleteMonth(range.getToDate(), range.getFromDt());
                    interval = lvRuleOnDate.getInterval();
                    partCredit = lvRuleOnDate.getPartCrdt();
                    creditCnt = lvRuleOnDate.getCrdCnt();
                    range.setCompleteMon(compltMon + "");
                    range.setNoofLeave(getNoofLeave(compltMon, interval, partCredit, creditCnt) + "");
                    al.add(range);
                }

            } else {
                break;
            }

        }
        for (int i = 0; i < al.size(); i++) {
            Range rg = new Range();
            rg = (Range) al.get(i);
            ArrayList tempSpcWise = new ArrayList();
            tempSpcWise = getSpcWiseInnerRange(empid, tolid, rg.getFromDt(), rg.getToDate());
            if (tempSpcWise.size() != 0) {
                for (int j = 0; j < tempSpcWise.size(); j++) {
                    Range rgSpc = new Range();
                    rgSpc = (Range) tempSpcWise.get(j);
                    alFinal.add(rgSpc);
                }
            } else {
                alFinal.add(rg);
            }
        }
        for (int i = 0; i < alFinal.size(); i++) {
            Range rg = new Range();
            rg = (Range) alFinal.get(i);
        }
        return alFinal;
    }

    public ArrayList getSpcWiseInnerRange(String empId, String tolid, String fromDate, String toDate) {
        Connection con = null;
        Statement st = null;
        Statement st1 = null;
        ArrayList spcWise = new ArrayList();
        ResultSet rs = null;
        String inpToDate = toDate;
        String spcFromDate = null;
        String spcToDate = null;
        String vacTypeFromDate = "";
        String vacTypeToDate = "";
        LeaveRule lvRuleOnDate = null;
        LeaveRule lvRuleTo = null;
        Range range = null;
        Range range1 = null;
        int compltMon = 0;
        float creditCnt = 0;
        float partCredit = 0;
        float interval = 0;
        try {
            con = dataSource.getConnection();
            st = con.createStatement();
            st1 = con.createStatement();

            int cnt = 0;
            rs = st1.executeQuery("SELECT SPC,JOIN_DATE FROM EMP_JOIN WHERE emp_id='" + empId + "' AND (JOIN_DATE >= '" + fromDate + "') AND (JOIN_DATE <= '" + toDate + "')");

            while (rs.next()) {

                range = new Range();
                toDate = CommonFunctions.getFormattedInputDate(rs.getDate("JOIN_DATE"));

                spcFromDate = getSPCOnParticularDate(empId, fromDate);
                spcToDate = getSPCOnParticularDate(empId, toDate);
                if (spcFromDate != null && !spcFromDate.equals("")) {

                    vacTypeFromDate = getPostVacType(spcFromDate.substring(13, 19));
                }

                if (spcToDate != null && !spcToDate.equals("")) {
                    vacTypeToDate = getPostVacType(spcToDate.substring(13, 19));
                }
                if (vacTypeFromDate == null) {
                    vacTypeFromDate = "";
                }
                if (vacTypeToDate == null) {
                    vacTypeToDate = "";
                }
                if (!spcFromDate.equals(spcToDate)) {
                    if (!vacTypeFromDate.equals(vacTypeToDate)) {

                        range.setFromDt(fromDate);
                        if (getPrevFromDate(toDate) != null) {

                            range.setToDate(CommonFunctions.getFormattedInputDate(getPrevFromDate(toDate)));
                            String subinputDate = "";
                            Date tempDate = new Date();
                            subinputDate = range.getToDate().substring(range.getToDate().indexOf("-") + 1, range.getToDate().lastIndexOf("-"));
                            if (subinputDate.length() == 2) {
                                tempDate = CommonFunctions.getFormattedInputDateType3(range.getToDate());
                                Calendar tempCal = Calendar.getInstance();
                                tempCal.set(tempDate.getYear() + 1900, tempDate.getMonth(), tempDate.getDate());
                                range.setToDate(CommonFunctions.getFormattedInputDate(tempDate));

                            }
                        } else {
                            range.setToDate("");
                        }

                        lvRuleOnDate = new LeaveRule();

                        lvRuleOnDate = getLeaveRule(empId, tolid, fromDate);
                        lvRuleTo = new LeaveRule();
                        lvRuleTo = getLeaveRule(empId, tolid, range.getToDate());
                        if ((!lvRuleOnDate.getLeaveId().equals(lvRuleTo.getLeaveId())) || (tolid.equals("AEL") && vacTypeFromDate.equals("A") && !vacTypeToDate.equals("A"))) {
//							
                            cnt++;
                            compltMon = getCompleteMonth(range.getToDate(), range.getFromDt());
                            interval = lvRuleOnDate.getInterval();
                            partCredit = lvRuleOnDate.getPartCrdt();
                            creditCnt = lvRuleOnDate.getCrdCnt();
                            range.setCompleteMon(compltMon + "");
                            range.setNoofLeave(getNoofLeave(compltMon, interval, partCredit, creditCnt) + "");
                            spcWise.add(range);
                        }
                        //SPECIFIC CONDITION FOR AEL
                    }
                }
//				fromDate = rs.getString("JOIN_DATE");
                fromDate = CommonFunctions.getFormattedOutputDate4(rs.getDate("JOIN_DATE"));
            }

            if (cnt > 0 && !fromDate.equals(inpToDate)) {
                range = new Range();
                range.setFromDt(fromDate);
                range.setToDate(inpToDate);
                lvRuleOnDate = new LeaveRule();
                lvRuleOnDate = getLeaveRule(empId, tolid, fromDate);

                if (lvRuleOnDate.getLeaveId() != null && !lvRuleOnDate.getLeaveId().equals("")) {
                    compltMon = getCompleteMonth(range.getToDate(), range.getFromDt());
                    interval = lvRuleOnDate.getInterval();
                    partCredit = lvRuleOnDate.getPartCrdt();
                    creditCnt = lvRuleOnDate.getCrdCnt();
                    range.setCompleteMon(compltMon + "");
                    range.setNoofLeave(getNoofLeave(compltMon, interval, partCredit, creditCnt) + "");
                    spcWise.add(range);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st, con);
        }
        return spcWise;
    }

    public String getToDate(String fromDate, int interval, String annualRound) {
        boolean flag = false;
        int curmonth = 0;
        String toDate = "";
        Date tempToDate = null;
        try {
            DateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
            Date tempFromDate = df.parse(fromDate);
            curmonth = tempFromDate.getMonth() + 1;
            for (int i = 1; i <= 12 && flag == false; i++) {
                if (interval > 0) {
                    if (i >= curmonth && i % (interval) == 0) {
                        flag = true;
                        Calendar clint = Calendar.getInstance();
                        clint.set(tempFromDate.getYear() + 1900, i, 1);
                        clint.add(Calendar.DATE, -1);
                        tempToDate = clint.getTime();

                    }
                }
            }

            if (tempToDate != null) {
                toDate = CommonFunctions.getFormattedOutputDate1(tempToDate);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return toDate;
    }

    public ArrayList getWefWiseInnerRange(String empId, String tolid, String fromDate, String toDate) {
        ArrayList wefWise = null;
        Connection con = null;
        ResultSet rs = null;
        LeaveRule lvFrmDate = null;
        LeaveRule lvToDate = null;
        LeaveRule lvRuleOnDate = null;
        Statement st = null;
        Statement st1 = null;
        int compltMon = 0;
        float creditCnt = 0;
        float partCredit = 0;
        float interval = 0;
        try {
            con = dataSource.getConnection();
            lvFrmDate = getLeaveRule(empId, tolid, fromDate);
            lvToDate = getLeaveRule(empId, tolid, toDate);

            if (!lvFrmDate.getLeaveId().equals(lvToDate.getLeaveId())) {
                wefWise = new ArrayList();
                st = con.createStatement();
                rs = st.executeQuery("SELECT WEF FROM G_LEAVE WHERE  WEF>= '" + fromDate + "' AND  WEF <='" + toDate + "'  AND TOL_ID='" + tolid + "' AND (VACATION IS NULL OR VACATION = '') ORDER BY WEF ASC");
                int cnt = 0;
                String tmpFrmDt = "";
                while (rs.next()) {
                    Range range = new Range();
                    if (cnt == 0) {
                        // modified by saroj (fromDate Parse Exception)
                        range.setFromDt(fromDate);
                        range.setToDate(CommonFunctions.getFormattedOutputDate1(getPrevFromDate(rs.getString("WEF"))));
                        lvRuleOnDate = getLeaveRule(empId, tolid, fromDate);
                        compltMon = getCompleteMonth(range.getToDate(), range.getFromDt());
                        interval = lvRuleOnDate.getInterval();
                        partCredit = lvRuleOnDate.getPartCrdt();
                        creditCnt = lvRuleOnDate.getCrdCnt();
                        range.setCompleteMon(compltMon + "");
                        range.setNoofLeave(getNoofLeave(compltMon, interval, partCredit, creditCnt) + "");
                    } else {
                        range.setFromDt(CommonFunctions.getFormattedDate(tmpFrmDt));
                        range.setToDate(CommonFunctions.getFormattedOutputDate1(getPrevFromDate(rs.getString("WEF"))));
                        lvRuleOnDate = getLeaveRule(empId, tolid, fromDate);
                        compltMon = getCompleteMonth(range.getToDate(), range.getFromDt());
                        interval = lvRuleOnDate.getInterval();
                        partCredit = lvRuleOnDate.getPartCrdt();
                        creditCnt = lvRuleOnDate.getCrdCnt();
                        range.setCompleteMon(compltMon + "");
                        range.setNoofLeave(getNoofLeave(compltMon, interval, partCredit, creditCnt) + "");
                    }
                    tmpFrmDt = rs.getString("WEF");
                    wefWise.add(range);
                    cnt++;
                }
                if ((tmpFrmDt != null && !tmpFrmDt.equals("")) && (toDate != null && !toDate.equals(""))) {
                    if (!CommonFunctions.getFormattedOutputDate(getPrevFromDate(tmpFrmDt)).equals(toDate)) {
                        Range range = new Range();
                        range.setFromDt(CommonFunctions.getFormattedDate(tmpFrmDt));
                        // modified by saroj (to Date Parse Exception)
                        range.setToDate(toDate);
                        lvRuleOnDate = getLeaveRule(empId, tolid, fromDate);
                        compltMon = getCompleteMonth(range.getToDate(), range.getFromDt());
                        interval = lvRuleOnDate.getInterval();
                        partCredit = lvRuleOnDate.getPartCrdt();
                        creditCnt = lvRuleOnDate.getCrdCnt();
                        range.setCompleteMon(compltMon + "");
                        range.setNoofLeave(getNoofLeave(compltMon, interval, partCredit, creditCnt) + "");
                        wefWise.add(range);
                    }
                }

//				
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st, con);
        }
        return wefWise;
    }

    public float getNoofLeave(float comMon, float interval, float partCredit, float creditLeave) {
        float creleave = 0;
        float nocreleave = 0;
        float mulp = 0;
        int nearestComMonth = 0;
        try {
            if (partCredit == 0) {
                partCredit = interval;
            }

            if (comMon == interval) {
                creleave = creditLeave;
            } //-------------Calculation for No of credited if no of complete months greater then part credit month----------------
            else if (comMon >= partCredit) {

                nocreleave = (creditLeave / interval);
                if (comMon % partCredit == 0) {
                    mulp = (nocreleave) * (comMon);
                } else {
                    nearestComMonth = (int) (comMon / partCredit);
                    if (nearestComMonth != 0) {
                        mulp = nocreleave * nearestComMonth * partCredit;
                    }
                }
                creleave = mulp;
            } else {
                creleave = 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return creleave;
    }

    public Date getPrevFromDate(String inputDate) {
        Date nxtPrevDate = null;
        String subinputDate = null;
        DateFormat df = null;
        Date tempDate = new Date();
        try {
            subinputDate = inputDate.substring(0, inputDate.indexOf("-"));
            if (subinputDate.length() == 2) {
                df = new SimpleDateFormat("dd-MMM-yyyy");
                tempDate = df.parse(inputDate);
            } else {
                df = new SimpleDateFormat("yyyy-MM-dd");
                tempDate = df.parse(inputDate);
            }
            Calendar tempCal = Calendar.getInstance();
            tempCal.set(tempDate.getYear() + 1900, tempDate.getMonth(), tempDate.getDate());
            tempCal.add(Calendar.DATE, -1);
            nxtPrevDate = tempCal.getTime();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return nxtPrevDate;
    }

    public int getCompleteMonth(String toDate, String fromDate) {
        int comMonth = 0;
        int dateOfMonth = 1;
        Date tempfromDate = null;
        Date temptoDate = null;

        try {
            DateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
            tempfromDate = df.parse(fromDate);
            temptoDate = df.parse(toDate);
            dateOfMonth = tempfromDate.getDate();

            if (dateOfMonth != 01) {

                if (temptoDate.getYear() == tempfromDate.getYear()) {
                    comMonth = ((temptoDate.getMonth()) - tempfromDate.getMonth());
                    if (temptoDate.getDate() < (tempfromDate.getDate() - 1)) {
                        comMonth--;
                    }
                } else if (temptoDate.getYear() > tempfromDate.getYear()) {

                    comMonth = temptoDate.getMonth() - tempfromDate.getMonth() + (12 * (temptoDate.getYear() - tempfromDate.getYear()));
                    if (temptoDate.getDate() < (tempfromDate.getDate() - 1)) {
                        comMonth--;
                    }
                }
            } else if (dateOfMonth == 01) {
                if (temptoDate.getYear() == tempfromDate.getYear()) {
                    comMonth = ((temptoDate.getMonth()) - tempfromDate.getMonth());
                    if (ifLastDayOfMonth(temptoDate)) {
                        comMonth++;
                    }
                } else if (temptoDate.getYear() > tempfromDate.getYear()) {
                    comMonth = ((temptoDate.getMonth()) - tempfromDate.getMonth()) + 12 * (temptoDate.getYear() - tempfromDate.getYear());
                    if (ifLastDayOfMonth(temptoDate)) {
                        comMonth++;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return comMonth;
    }

    public boolean ifLeapYear(int theYear) {
        boolean res = false;
        try {
            // Is theYear Divisible by 4?
            if (theYear % 4 == 0) {
                // Is theYear Divisible by 4 but not 100?
                if (theYear % 100 != 0) {

                    res = true;
                } // Is theYear Divisible by 4 and 100 and 400?
                else if (theYear % 400 == 0) {

                    res = true;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public boolean ifLastDayOfMonth(Date cal) {
        boolean res = false;
        if (cal.getMonth() == 0 || cal.getMonth() == 2 || cal.getMonth() == 4 || cal.getMonth() == 6 || cal.getMonth() == 7 || cal.getMonth() == 9 || cal.getMonth() == 11) {
            if (cal.getDate() == 31) {
                res = true;
            }
        } else if (cal.getMonth() == 3 || cal.getMonth() == 5 || cal.getMonth() == 8 || cal.getMonth() == 10) {
            if (cal.getDate() == 30) {
                res = true;
            }
        } else if (cal.getMonth() == 1) {
            if (ifLeapYear(cal.getYear())) {
                if (cal.getDate() == 29) {
                    res = true;
                }
            } else if (cal.getDate() == 28) {
                res = true;
            }
        }
        return res;
    }

    public String getToDate(String fromDate, int interval) throws Exception {
        boolean flag = false;
        int curmonth = 0;
        String toDate = "";
        Date tempToDate = null;
        try {
            DateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
            Date tempFromDate = df.parse(fromDate);
            curmonth = tempFromDate.getMonth() + 1;
            for (int i = 1; i <= 12 && flag == false; i++) {
                if (interval > 0) {
                    if (i >= curmonth && i % (interval) == 0) {
                        flag = true;
                        Calendar clint = Calendar.getInstance();
                        clint.set(tempFromDate.getYear() + 1900, i, 1);
                        clint.add(Calendar.DATE, -1);
                        tempToDate = clint.getTime();

                    }
                }
            }

            if (tempToDate != null) {
                toDate = CommonFunctions.getFormattedOutputDate1(tempToDate);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return toDate;
    }

    public Date getNxtFromDate(String inputDate) {
        Date nxtFrmDate = null;
        try {
            DateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
            Date tempDate = df.parse(inputDate);
            Calendar tempCal = Calendar.getInstance();
            tempCal.set(tempDate.getYear() + 1900, tempDate.getMonth(), tempDate.getDate());
            tempCal.add(Calendar.DATE, 1);
            nxtFrmDate = tempCal.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nxtFrmDate;
    }

    public ArrayList getLeaveCreditList(String empId, String tolid) {
        ArrayList leaveCreditList = new ArrayList();
        CreditLeaveProperties clv = getLeaveOBDate(empId, tolid);
        String obDate = clv.getOpenbaldate();
        String obNextDate = clv.getNextopenbaldate();
        String saDate = getSADate(empId);
        String startForcastingDate = "";
        String endForcastingDate = "";
        if (obDate != null && !obDate.equals("")) {
            if (clv.getFnan() != null && !clv.getFnan().equals("")) {
                if (clv.getFnan().equals("FN")) {
                    startForcastingDate = obDate;
                } else {
                    startForcastingDate = obNextDate;
                }
            }
        }
        if (saDate != null && !saDate.equals("")) {
            endForcastingDate = saDate;
        }
        if (startForcastingDate != null && !startForcastingDate.equals("") && endForcastingDate != null && !endForcastingDate.equals("")) {

        }
        return leaveCreditList;
    }

    public String getWefToBeApplied(String inputDate, String tolId) {
        String wefDate = "";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("SELECT MAX(WEF) WEFDATE FROM G_LEAVE WHERE TOL_ID='" + tolId + "' AND WEF<='" + inputDate + "'");
            rs = ps.executeQuery();
            if (rs.next()) {
                if (rs.getString("WEFDATE") != null && !rs.getString("WEFDATE").equals("")) {
                    wefDate = rs.getString("WEFDATE");
                }
            }

            if (wefDate.equals("") || wefDate == null) {
                rs = ps.executeQuery("SELECT MIN(WEF) WEFDATE FROM G_LEAVE  WHERE TOL_ID='" + tolId + "' AND WEF>='" + inputDate + "'");
                while (rs.next()) {
                    if (rs.getString("WEFDATE") != null && !rs.getString("WEFDATE").equals("")) {
                        wefDate = rs.getString("WEFDATE");
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps, con);
        }
        return wefDate;
    }

    public String getPostVacType(String postCode) {
        String vacType = "";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = this.dataSource.getConnection();
            if (postCode != null && !postCode.equals("")) {
                ps = con.prepareStatement("SELECT VACATION FROM G_POST WHERE POST_CODE=?");
                ps.setString(1, postCode);
                rs = ps.executeQuery();
                if (rs.next()) {
                    vacType = rs.getString("VACATION");
                }
            } else {
                vacType = "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps, con);
        }
        return vacType;
    }

    @Override
    public LeaveRule getLeaveRule(String empId, String typeLeave, String inputDate) {
        LeaveRule leaveRule = new LeaveRule();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String spc = null;
        String wef = null;
        String vacType = null;
        try {
            con = this.dataSource.getConnection();
            spc = getSPCOnParticularDate(empId, inputDate);
            wef = getWefToBeApplied(inputDate, typeLeave);
            if (spc != null && !spc.equals("")) {
                vacType = getPostVacType(spc.substring(13, 19));
            }
            int cnt = 0;

            if (vacType != null && !vacType.equals("")) {
                ps = con.prepareStatement("SELECT LVID,CINTERVAL, CREDIT_CNT, PART_CREDIT, MAX_CR,WEF,ANNUAL_ROUND,WHEN_CREDIT FROM G_LEAVE WHERE TOL_ID='" + typeLeave + "' AND WEF='" + wef + "' AND VACATION='" + vacType + "'");
                rs = ps.executeQuery();
            } else {
                ps = con.prepareStatement("SELECT LVID,CINTERVAL, CREDIT_CNT, PART_CREDIT, MAX_CR,WEF,ANNUAL_ROUND,WHEN_CREDIT FROM G_LEAVE WHERE TOL_ID='" + typeLeave + "' AND WEF='" + wef + "' AND VACATION IS NULL");
                rs = ps.executeQuery();
            }
            while (rs.next()) {
                cnt++;
                leaveRule.setCrdCnt(rs.getFloat("CREDIT_CNT"));
                leaveRule.setInterval(rs.getInt("CINTERVAL"));
                leaveRule.setLeaveId(rs.getString("LVID"));
                leaveRule.setPartCrdt(rs.getInt("PART_CREDIT"));
                leaveRule.setAnnualRound(rs.getString("ANNUAL_ROUND"));
                leaveRule.setWhen_credit(rs.getString("WHEN_CREDIT"));
                if (rs.getString("WEF") != null && !rs.getString("WEF").equals("")) {
                    leaveRule.setWefDate(rs.getString("WEF"));
                }
            }

            if (cnt == 0) {
                ps = con.prepareStatement("SELECT LVID,CINTERVAL, CREDIT_CNT, PART_CREDIT, MAX_CR,WEF,ANNUAL_ROUND,WHEN_CREDIT FROM G_LEAVE WHERE TOL_ID='" + typeLeave + "' AND WEF='" + wef + "' AND VACATION IS NULL");
                rs = ps.executeQuery();
                while (rs.next()) {
                    leaveRule.setCrdCnt(rs.getFloat("CREDIT_CNT"));
                    leaveRule.setInterval(rs.getInt("CINTERVAL"));
                    leaveRule.setLeaveId(rs.getString("LVID"));
                    leaveRule.setPartCrdt(rs.getInt("PART_CREDIT"));
                    leaveRule.setAnnualRound(rs.getString("ANNUAL_ROUND"));
                    leaveRule.setWhen_credit(rs.getString("WHEN_CREDIT"));
                    if (rs.getString("WEF") != null && !rs.getString("WEF").equals("")) {
                        leaveRule.setWefDate(rs.getString("WEF"));
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return leaveRule;
    }

    @Override
    public List getLeaveList(String empId, String leaveType) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "";
        List li = new ArrayList();
        try {

            sql = " SELECT * FROM (SELECT * FROM(SELECT LVID,LNOTID,LNOTYPE,LSOSTID,LTOLID,LFDATE,LTDATE,LSUF,LSUFRM,LPREFIX,LPREFIXTO,LSYEAR,LTYEAR,LSDAYS,NOT_ID,NOT_TYPE,EMP_ID,DOE,TOE,IF_ASSUMED,ORDNO,ORDDT,DEPT_CODE,OFF_CODE,AUTH,ENT_DEPT,ENT_OFF,ENT_AUTH,NOTE,ISCANCELED,LINK_ID,IF_VISIBLE,CANCEL_TYPE,IF_MEDICAL,IF_COMMUTED,IF_LONGTERM,TR_DATA_TYPE ,SV_ID FROM (SELECT emp_leave.leaveid lvid,emp_leave.not_id lnotid,emp_leave.not_type lnotype,emp_leave.lsot_id "
                    + " lsostid,emp_leave.tol_id ltolid,emp_leave.fdate lfdate,emp_leave.tdate ltdate,emp_leave.suffix_date lsuf,emp_leave.suffix_from "
                    + " lsufrm,emp_leave.prefix_date lprefix,emp_leave.prefix_to lprefixto,emp_leave.s_year lsyear,emp_leave.t_year ltyear,emp_leave.if_medical if_medical,emp_leave.if_commuted if_commuted,emp_leave.if_longterm if_longterm,"
                    + " emp_leave.s_days lsdays  FROM emp_leave where EMP_ID=? AND LSOT_ID=?) leav inner join "
                    + " (select e1.*,e2.not_type cancel_type from (select * from emp_notification where EMP_ID=?) e1 left outer join "
                    + " (select * from emp_notification where EMP_ID=?) e2 on e1.link_id=e2.not_id)"
                    + " emp_notification on emp_notification.not_type = lnotype and emp_notification.not_id = lnotid ORDER BY DOE DESC)leaveList)leavetemp ";

            con = this.dataSource.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, empId);
            ps.setString(2, leaveType);
            ps.setString(3, empId);
            ps.setString(4, empId);
            rs = ps.executeQuery();

            while (rs.next()) {

                Leave leave = new Leave();

                if (rs.getString("lnotid") != null && !rs.getString("lnotid").equals("")) {
                    leave.setNotid(rs.getString("lnotid"));
                }
                if (rs.getString("lnotype") != null && !rs.getString("lnotype").equals("")) {
                    leave.setNotType(rs.getString("lnotype"));
                }

                if (rs.getString("LINK_ID") != null && !rs.getString("LINK_ID").equals("")) {
                    leave.setCancelNotId(rs.getString("LINK_ID"));
                }
                if (rs.getString("ORDNO") != null && !rs.getString("ORDNO").equals("")) {
                    leave.setTxtOrdNo(rs.getString("ORDNO"));
                }
                if (rs.getDate("ORDDT") != null && !rs.getDate("ORDDT").equals("")) {
                    leave.setTxtOrdDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("ORDDT")));
                }

                if (rs.getDate("lfdate") != null && !rs.getString("lfdate").equals("")) {
                    leave.setTxtperiodFrom(CommonFunctions.getFormattedOutputDate1(rs.getDate("lfdate")));
                } else {
                    leave.setTxtperiodFrom("");
                }
                if (rs.getDate("ltdate") != null && !rs.getString("ltdate").equals("")) {
                    leave.setTxtperiodTo(CommonFunctions.getFormattedOutputDate1(rs.getDate("ltdate")));
                } else {
                    leave.setTxtperiodTo("");
                }
                if (rs.getDate("lsuf") != null && !rs.getString("lsuf").equals("")) {
                    leave.setTxtsuffixTo(CommonFunctions.getFormattedOutputDate1(rs.getDate("lsuf")));
                }
                if (rs.getDate("lsufrm") != null && !rs.getString("lsufrm").equals("")) {
                    leave.setTxtsuffixFrom(CommonFunctions.getFormattedOutputDate1(rs.getDate("lsufrm")));
                }
                if (rs.getDate("lprefix") != null && !rs.getString("lprefix").equals("")) {
                    leave.setTxtprefixFrom(CommonFunctions.getFormattedOutputDate1(rs.getDate("lprefix")));
                }
                if (rs.getDate("lprefixto") != null && !rs.getString("lprefixto").equals("")) {
                    leave.setTxtprefixTo(CommonFunctions.getFormattedOutputDate1(rs.getDate("lprefixto")));
                }
                if (rs.getDate("DOE") != null && !rs.getString("DOE").equals("")) {
                    leave.setServicedate(CommonFunctions.getFormattedOutputDate1(rs.getDate("DOE")));
                }
                if (rs.getString("lsyear") != null && !rs.getString("lsyear").equals("")) {
                    leave.setSyear(rs.getString("lsyear"));
                }

                if (rs.getString("IF_MEDICAL") != null && !rs.getString("IF_MEDICAL").equals("")) {
                    leave.setIfmedical(rs.getString("IF_MEDICAL"));
                }
                if (rs.getString("IF_COMMUTED") != null && !rs.getString("IF_COMMUTED").equals("")) {
                    leave.setIfcommuted(rs.getString("IF_COMMUTED"));
                }

                leave.setChkLongTerm(rs.getString("IF_LONGTERM"));
                li.add(leave);
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
    public boolean checkHalfPayLeave(String leaveType) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean lType = false;
        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("SELECT hpl FROM G_LEAVE_SETUP WHERE  hpl=?");
            ps.setString(1, leaveType);
            rs = ps.executeQuery();
            while (rs.next()) {
                lType = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return lType;
    }

    @Override
    public boolean checkEarnedLeave(String leaveType) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean lType = false;
        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("SELECT EL FROM G_LEAVE_SETUP WHERE  EL=?");
            ps.setString(1, leaveType);
            rs = ps.executeQuery();
            while (rs.next()) {
                lType = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return lType;
    }

    @Override
    public ArrayList getAvailedLeaveList(String empId, String leaveType) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList availedLeaveList = new ArrayList();
        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("SELECT FDATE,TDATE,EXTRACT(DAY FROM TDATE-FDATE)+1 as NO_OF_DAYS,LSOT_ID FROM EMP_LEAVE WHERE NOT_ID NOT IN "
                    + "(SELECT NOT_ID FROM EMP_NOTIFICATION WHERE EMP_ID=? AND ISCANCELED='Y') AND EMP_ID=? AND (TOL_ID=? or LSOT_ID='06') ORDER BY FDATE");
            ps.setString(1, empId);
            ps.setString(2, empId);
            ps.setString(3, leaveType);
            rs = ps.executeQuery();
            while (rs.next()) {
                EmpLeaveAvailedPropeties empLeaveAvailedProperties = new EmpLeaveAvailedPropeties();
                empLeaveAvailedProperties.setFromdate(CommonFunctions.getFormattedOutputDate1(rs.getDate("FDATE")));
                empLeaveAvailedProperties.setTodate(CommonFunctions.getFormattedOutputDate1(rs.getDate("TDATE")));
                empLeaveAvailedProperties.setLsotId(rs.getString("LSOT_ID"));
                empLeaveAvailedProperties.setTolId(leaveType);

                if (!rs.getString("LSOT_ID").equals("02")) {
                    empLeaveAvailedProperties.setTotalNoofdays(rs.getInt("NO_OF_DAYS"));
                } else {
                    empLeaveAvailedProperties.setTotalNoofdays(0);
                }

                availedLeaveList.add(empLeaveAvailedProperties);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return availedLeaveList;
    }

    public ArrayList getSurrenderedLeaveList(String empId, String tolId) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList surrenderedLeaveList = new ArrayList();
        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("SELECT TO_DATE('01' || '-JAN-' || S_YEAR,'DD-MON-YYYY') AS FDATE,S_DAYS  FROM EMP_LEAVE WHERE NOT_ID NOT IN "
                    + "(SELECT NOT_ID FROM EMP_NOTIFICATION WHERE EMP_ID=? AND ISCANCELED='Y') AND EMP_ID=? AND LSOT_ID='02' AND TOL_ID=?");
            ps.setString(1, empId);
            ps.setString(2, empId);
            ps.setString(3, tolId);
            rs = ps.executeQuery();
            while (rs.next()) {
                EmpLeaveSurrenderedPropeties empLeaveSurrenderedPropeties = new EmpLeaveSurrenderedPropeties();
                empLeaveSurrenderedPropeties.setSurrenderDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("FDATE")));
                empLeaveSurrenderedPropeties.setSurrenderDays(rs.getInt("S_DAYS"));
                surrenderedLeaveList.add(empLeaveSurrenderedPropeties);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return surrenderedLeaveList;
    }

    private ArrayList getLeaveAvailedInThePeriod(ArrayList<EmpLeaveAvailedPropeties> availedLeaveList, String spfrom, String spto, String tolId) {
        ArrayList availedLeaveListInPeriod = new ArrayList();
        long diff;
        Date leaveCreditedFromDate = CommonFunctions.getDateFromString(spfrom, "dd-MMM-yyyy");
        Date leaveCreditedToDate = CommonFunctions.getDateFromString(spto, "dd-MMM-yyyy");

        for (int i = 0; i < availedLeaveList.size(); i++) {
            EmpLeaveAvailedPropeties empLeaveAvailedProperties = availedLeaveList.get(i);
            Date leaveAvailedFromDate = CommonFunctions.getDateFromString(empLeaveAvailedProperties.getFromdate(), "dd-MMM-yyyy");
            Date leaveAvailedToDate = CommonFunctions.getDateFromString(empLeaveAvailedProperties.getTodate(), "dd-MMM-yyyy");
            if (leftoutempLeaveAvailedProperties != null) {
                Date leftOutLeaveFromDate = CommonFunctions.getDateFromString(leftoutempLeaveAvailedProperties.getFromdate(), "dd-MMM-yyyy");
                Date leftOutLeaveToDate = CommonFunctions.getDateFromString(leftoutempLeaveAvailedProperties.getTodate(), "dd-MMM-yyyy");
                if (leaveCreditedFromDate.compareTo(leftOutLeaveFromDate) <= 0 && leaveCreditedToDate.compareTo(leftOutLeaveFromDate) >= 0 && leaveCreditedFromDate.compareTo(leftOutLeaveToDate) <= 0 && leaveCreditedToDate.compareTo(leftOutLeaveToDate) >= 0) {
                    availedLeaveListInPeriod.add(leftoutempLeaveAvailedProperties);
                    leftoutempLeaveAvailedProperties = null;
                }
            }

            if (leaveCreditedFromDate.compareTo(leaveAvailedFromDate) <= 0 && leaveCreditedToDate.compareTo(leaveAvailedFromDate) >= 0 && leaveCreditedFromDate.compareTo(leaveAvailedToDate) <= 0 && leaveCreditedToDate.compareTo(leaveAvailedToDate) >= 0 && empLeaveAvailedProperties.getTolId().equals(tolId)) {
                availedLeaveListInPeriod.add(empLeaveAvailedProperties);
            } else if (leaveCreditedFromDate.compareTo(leaveAvailedFromDate) <= 0 && leaveCreditedToDate.compareTo(leaveAvailedFromDate) >= 0 && leaveCreditedFromDate.compareTo(leaveAvailedToDate) <= 0 && leaveCreditedToDate.compareTo(leaveAvailedToDate) < 0) {
                empLeaveAvailedProperties.setTodate(empLeaveAvailedProperties.getTodate());
                if (!empLeaveAvailedProperties.getLsotId().equals("02")) {
                    diff = leaveCreditedToDate.getTime() - leaveAvailedFromDate.getTime();
                    empLeaveAvailedProperties.setTotalNoofdays(Integer.parseInt((TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) + 1) + ""));
                } else {
                    diff = 0;
                    empLeaveAvailedProperties.setTotalNoofdays(Integer.parseInt((TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) + 0) + ""));
                }

                availedLeaveListInPeriod.add(empLeaveAvailedProperties);

                Date leftOutLeaveFromDate = DateUtils.addDays(leaveCreditedToDate, 1);
                Date leftOutLeaveToDate = leaveAvailedToDate;

                diff = (leftOutLeaveToDate.getTime() - leftOutLeaveFromDate.getTime());

                leftoutempLeaveAvailedProperties = new EmpLeaveAvailedPropeties();
                leftoutempLeaveAvailedProperties.setFromdate(CommonFunctions.getFormattedOutputDate1(leftOutLeaveFromDate));
                leftoutempLeaveAvailedProperties.setTodate(CommonFunctions.getFormattedOutputDate1(leftOutLeaveToDate));

                leftoutempLeaveAvailedProperties.setTotalNoofdays(Integer.parseInt((TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) + 1) + ""));
            }
        }
        return availedLeaveListInPeriod;
    }

    private ArrayList getLeaveSurrenderedInThePeriod(ArrayList<EmpLeaveSurrenderedPropeties> surrenderedLeaveList, String spfrom, String spto) {
        ArrayList surrenderedLeaveListInPeriod = new ArrayList();
        Date leaveCreditedFromDate = CommonFunctions.getDateFromString(spfrom, "dd-MMM-yyyy");
        Date leaveCreditedToDate = CommonFunctions.getDateFromString(spto, "dd-MMM-yyyy");
        for (int i = 0; i < surrenderedLeaveList.size(); i++) {
            EmpLeaveSurrenderedPropeties empLeaveSurrenderedPropeties = surrenderedLeaveList.get(i);
            Date leaveSurrenderedFromDate = CommonFunctions.getDateFromString(empLeaveSurrenderedPropeties.getSurrenderDate(), "dd-MMM-yyyy");
            if (leaveSurrenderedFromDate.compareTo(leaveCreditedFromDate) >= 0 && leaveCreditedToDate.compareTo(leaveSurrenderedFromDate) > 0) {
                surrenderedLeaveListInPeriod.add(empLeaveSurrenderedPropeties);
            }
        }
        return surrenderedLeaveListInPeriod;
    }

    public LeaveTypeBean getMaxCreditLimitofLeave(String leaveType) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        LeaveTypeBean leaveTypeBean = new LeaveTypeBean();
        try {
            con = this.dataSource.getConnection();
            if (leaveType.equals("COL")) {
                ps = con.prepareStatement("SELECT DISTINCT TOL,MAX_CR FROM G_LEAVE where TOL_ID=?");
                ps.setString(1, "HPL");
            } else {
                ps = con.prepareStatement("SELECT DISTINCT TOL,MAX_CR FROM G_LEAVE where TOL_ID=?");
                ps.setString(1, leaveType);
            }
            rs = ps.executeQuery();
            if (rs.next()) {
                if (leaveType.equals("COL")) {
                    leaveTypeBean.setTol("COMMUTED LEAVE");
                } else {
                    leaveTypeBean.setTol(rs.getString("TOL").toUpperCase());
                }
                leaveTypeBean.setMaximumLimit(rs.getInt("MAX_CR"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps, con);
        }
        return leaveTypeBean;
    }

    public LeaveOpeningBalanceBean getLeaveOpeningBalance(String empId, String leaveType) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        LeaveOpeningBalanceBean lob = new LeaveOpeningBalanceBean();
        try {
            con = this.dataSource.getConnection();
            if (leaveType.equals("COL")) {
                ps = con.prepareStatement("SELECT * FROM EMP_LEAVE_OB WHERE EMP_ID=? AND TOL_ID=?");
                ps.setString(1, empId);
                ps.setString(2, "COL");
            } else if (leaveType.equals("HPL")) {
                ps = con.prepareStatement("SELECT * FROM EMP_LEAVE_OB WHERE EMP_ID=? AND TOL_ID=?");
                ps.setString(1, empId);
                ps.setString(2, "HPL");
            } else {
                ps = con.prepareStatement("SELECT * FROM EMP_LEAVE_OB WHERE EMP_ID=? AND TOL_ID=?");
                ps.setString(1, empId);
                ps.setString(2, leaveType);
            }

            rs = ps.executeQuery();
            if (rs.next()) {
                lob.setOpeningBalanceDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("OB_DATE")).toUpperCase());
                lob.setTime(rs.getString("OB_TIME"));
                lob.setOpeningBalance(rs.getString("OB"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps, con);
        }
        return lob;
    }

    @Override
    public EmpLeaveAccountPropeties getLeaveAccountDetails(String empId, String fDate, String tDate, String leaveType, String exOrdLeave) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        PreparedStatement stmt = null;
        //ResultSet rs = null;
        ResultSet emprs = null;
        EmpLeaveAccountPropeties eap = new EmpLeaveAccountPropeties();
        try {
            ArrayList crLeaveList = new ArrayList();
            con = this.dataSource.getConnection();
            String empInfoString = "";
            String empName = "";
            String inputSpFrom = null;
            String spFrom = null;
            String spTo = null;
            String spcFromDate = null;
            String vacTypeFromDate = "";
            String inputSpTO = null;
            int carryForward = 0;
            int totalEol = 0;
            long creditBalShow = 0;
            long creditBalance1 = 0;
            ArrayList availedLeaveList = getAvailedLeaveList(empId, leaveType);
            ArrayList surrenderedLeaveList = getSurrenderedLeaveList(empId, leaveType);
            ArrayList<Date> fDateofEmpleave = new ArrayList<Date>();
            ArrayList<Date> tDateofEmpleave = new ArrayList<Date>();

            /*Get Employee Details*/
            ps = con.prepareStatement("SELECT EMP_ID,GPF_NO,INITIALS,F_NAME,M_NAME,L_NAME,CUR_CADRE_NAME,ALLOTMENT_YEAR,OTHER_LEVEL FROM EMP_MAST WHERE EMP_ID=?");
            ps.setString(1, empId);
            rs = ps.executeQuery();
            if (rs.next()) {
                eap.setEmpId(rs.getString("EMP_ID"));
                eap.setEmpGpf(rs.getString("GPF_NO"));
                if (rs.getString("INITIALS") != null) {
                    empInfoString = empInfoString + rs.getString("INITIALS");
                    empName = empName + rs.getString("INITIALS");
                }
                if (rs.getString("F_NAME") != null) {
                    empInfoString = empInfoString + " " + rs.getString("F_NAME");
                    empName = empName + " " + rs.getString("F_NAME");
                }
                if (rs.getString("M_NAME") != null) {
                    empInfoString = empInfoString + " " + rs.getString("M_NAME");
                    empName = empName + " " + rs.getString("M_NAME");
                }
                if (rs.getString("L_NAME") != null) {
                    empInfoString = empInfoString + " " + rs.getString("L_NAME");
                    empName = empName + " " + rs.getString("L_NAME");
                }
                if (rs.getString("GPF_NO") != null) {
                    empInfoString = empInfoString + " (" + rs.getString("GPF_NO") + ") ";
                }
                if (rs.getString("CUR_CADRE_NAME") != null && !rs.getString("CUR_CADRE_NAME").trim().equals("")) {
                    //empInfoString = empInfoString + ", " + ServiceHistoryReport.getCadreName(rs.getString("CUR_CADRE_NAME"), stCadre);
                }
                if (rs.getString("OTHER_LEVEL") != null && !rs.getString("OTHER_LEVEL").trim().equals("")) {
                    empInfoString = empInfoString + ", (" + rs.getString("OTHER_LEVEL");
                    if (rs.getString("ALLOTMENT_YEAR") != null && !rs.getString("ALLOTMENT_YEAR").trim().equals("")) {
                        empInfoString = empInfoString + "-" + rs.getString("ALLOTMENT_YEAR");
                    }
                    empInfoString = empInfoString + ")";
                }
                eap.setEmpName(empName);
                eap.setEmpInfo(empInfoString);
            }
            DataBaseFunctions.closeSqlObjects(rs, ps);
            /*Get Employee Details*/
            long maximumLimit = 0;
            long credtBalance = 0;
            eap.setLeaveTypeId(leaveType);

            LeaveTypeBean leaveTypeBean = getMaxCreditLimitofLeave(leaveType);
            eap.setLeaveType(leaveTypeBean.getTol());

            LeaveOpeningBalanceBean lob = getLeaveOpeningBalance(empId, leaveType);
            eap.setLeaveOBalDate(lob.getOpeningBalanceDate());
            eap.setFnan(lob.getTime());
            if (lob.getOpeningBalance() != null) {
                eap.setLeaveOBal(Long.parseLong(lob.getOpeningBalance()));
            }

            if (eap.getLeaveOBal() != null) {
                credtBalance = eap.getLeaveOBal();
            }
            if (!fDate.equals("") && !tDate.equals("")) {

            } else {
                if (leaveType.equals("COL")) {
                    ps = con.prepareStatement("SELECT LCR_ID,EMP_ID,TOL_ID,SP_FROM,SP_TO,COM_MON,CR_COUNT,CR_TYPE,'1' SL FROM EMP_LEAVE_CR WHERE EMP_ID=? AND TOL_ID=? AND CR_TYPE='G'  ORDER BY SP_FROM");
                    ps.setString(1, empId);
                    ps.setString(2, leaveType);
                    rs = ps.executeQuery();
                } else if (leaveType.equals("HPL")) {
                    ps = con.prepareStatement("SELECT LCR_ID,EMP_ID,TOL_ID,SP_FROM,SP_TO,COM_MON,CR_COUNT,CR_TYPE,'1' SL FROM EMP_LEAVE_CR WHERE EMP_ID=? AND TOL_ID=? AND CR_TYPE='G'  ORDER BY SP_FROM");
                    ps.setString(1, empId);
                    ps.setString(2, leaveType);
                    rs = ps.executeQuery();
                } else if (leaveType.equals("EL")) {
                    ps = con.prepareStatement("SELECT SP_FROM,SP_TO,LCR_ID,EMP_ID,TOL_ID,COM_MON,CR_COUNT,CR_TYPE,'1' SL  "
                            + "FROM EMP_LEAVE_CR  WHERE EMP_ID=? AND (TOL_ID='EL' OR TOL_ID='AEL') AND CR_TYPE='G' ORDER BY SP_TO,COM_MON");
                    ps.setString(1, empId);
                    //ps.setString(2, leaveType);
                    rs = ps.executeQuery();
                } else {
                    ps = con.prepareStatement("SELECT DISTINCT ON ( SP_FROM)SP_FROM,( SP_TO)SP_TO,LCR_ID,EMP_ID,TOL_ID,COM_MON,CR_COUNT,CR_TYPE,'1' SL  "
                            + "FROM EMP_LEAVE_CR  WHERE EMP_ID=? AND TOL_ID=?  AND CR_TYPE='G' ORDER BY SP_FROM");
                    ps.setString(1, empId);
                    ps.setString(2, leaveType);
                    rs = ps.executeQuery();
                }
            }
            while (rs.next()) {
                Date spfrom = null;
                Date spto = null;
                String tempSpFrom = null;
                CreditLeaveProperties clp = new CreditLeaveProperties();
                long dummyLvBalCnt = 0;
                clp.setCreditType(rs.getString("CR_TYPE").toUpperCase());
                tempSpFrom = CommonFunctions.getFormattedOutputDate1(rs.getDate("SP_FROM")).toUpperCase();
                spfrom = new Date(tempSpFrom);
                clp.setTolId(rs.getString("TOL_ID"));

                if (rs.getString("SL").equalsIgnoreCase("1")) {
                    clp.setIfaddEL("N");
                    if (clp.getCreditType().equalsIgnoreCase("G")) {
                        clp.setFromDate(tempSpFrom);
                        clp.setCreditType(rs.getString("CR_TYPE").toUpperCase());
                    } else if (clp.getCreditType().equalsIgnoreCase("U")) {
                        clp.setFromDate(tempSpFrom);
                        clp.setCreditType(rs.getString("CR_TYPE").toUpperCase());
                    } else if (clp.getCreditType().equalsIgnoreCase("V")) {
                        clp.setFromDate(tempSpFrom);
                        clp.setCreditType(rs.getString("CR_TYPE").toUpperCase());
                    } else {
                        clp.setFromDate(tempSpFrom);
                        clp.setCreditType("G");
                    }
                } else {
                    clp.setIfaddEL("Y");
                    clp.setFromDate(tempSpFrom);
                    clp.setCreditType(rs.getString("TOL_ID"));
                }
                inputSpFrom = tempSpFrom;
                spcFromDate = getSPCOnParticularDate(empId, tempSpFrom);
                if (spcFromDate != null && !spcFromDate.equals("")) {
                    vacTypeFromDate = getPostVacType(spcFromDate.substring(13, 19));
                }
                maximumLimit = getMaxCreditLimitofLeave(leaveType, CommonFunctions.getFormattedOutputDate1(rs.getDate("SP_FROM")), vacTypeFromDate);

                String abc = null;
                if (rs.getDate("SP_TO") != null) {
                    spTo = rs.getString("SP_TO");
                    abc = CommonFunctions.getFormattedOutputDate1(rs.getDate("SP_TO")).toUpperCase();
                    clp.setToDate(abc);
                    spto = new Date(abc);
                    inputSpTO = abc;
                }
                String compMonth = rs.getString("COM_MON");
                clp.setCompMonths(compMonth);
                int completeMonth = Integer.parseInt(compMonth);
                String creditedLeave = rs.getString("CR_COUNT");
                clp.setLeaveCredited(Long.toString((Math.round(Double.parseDouble(creditedLeave)))));

                /*Get Leave Availed in the Period*/
                ArrayList<EmpLeaveAvailedPropeties> leaveAvailedInThePeriod = getLeaveAvailedInThePeriod(availedLeaveList, clp.getFromDate(), clp.getToDate(), clp.getTolId());
                /*Get Leave Surrendered in the Period*/
                ArrayList<EmpLeaveSurrenderedPropeties> surrenderedLeaveListInPeriod = getLeaveSurrenderedInThePeriod(surrenderedLeaveList, clp.getFromDate(), clp.getToDate());
                clp.setAvailedLeave(leaveAvailedInThePeriod);
                clp.setSurrenderedLeave(surrenderedLeaveListInPeriod);

                /*Start of Extra ordinary Leave Account */
                /*End of Extra ordinary Leave Account */
                clp.setTotEOLNumber(Integer.toString(totalEol));
                double leaveDed = (totalEol / 11);//Leave to be Deducted
                double roundValue = 0;
                roundValue = Math.round(leaveDed);
                clp.setLeaveDeduct(Long.toString((long) roundValue));

                credtBalance = credtBalance + (Math.round(Double.parseDouble(creditedLeave)) - (long) roundValue);
                creditBalShow = credtBalance;
                creditBalance1 = credtBalance;
                if (credtBalance > maximumLimit && maximumLimit != 0) {
                    credtBalance = maximumLimit;
                }

                clp.setCreditBalShow(Long.toString(credtBalance));
                for (int leaveCnt = 0; leaveCnt < leaveAvailedInThePeriod.size(); leaveCnt++) {
                    EmpLeaveAvailedPropeties empLeaveAvailedProperties = leaveAvailedInThePeriod.get(leaveCnt);
                    if (credtBalance == maximumLimit) {
                        if (leaveType.equals("COL")) {
                            credtBalance = (credtBalance + 10) - empLeaveAvailedProperties.getTotalNoofdays();
                        } else {
                            credtBalance = (credtBalance + 15) - empLeaveAvailedProperties.getTotalNoofdays();
                        }

                    } else {
                        if (empLeaveAvailedProperties.getLsotId() != null && empLeaveAvailedProperties.getLsotId().equals("06")) {
                            credtBalance = credtBalance + empLeaveAvailedProperties.getTotalNoofdays();
                        } else {
                            credtBalance = credtBalance - empLeaveAvailedProperties.getTotalNoofdays();
                        }
                    }
                    if (credtBalance < 0) {
                        credtBalance = 0;
                    }

                    empLeaveAvailedProperties.setBalanceLeave(credtBalance);
                }
                for (int leaveCnt = 0; leaveCnt < surrenderedLeaveListInPeriod.size(); leaveCnt++) {
                    EmpLeaveSurrenderedPropeties empLeaveSurrenderedPropeties = surrenderedLeaveListInPeriod.get(leaveCnt);
                    credtBalance = credtBalance - empLeaveSurrenderedPropeties.getSurrenderDays();
                    empLeaveSurrenderedPropeties.setBalanceLeave(credtBalance);
                }
                crLeaveList.add(clp);
            }
            eap.setCreditLvList(crLeaveList);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);

            DataBaseFunctions.closeSqlObjects(con);
        }

        return eap;
    }

    public long getMaxCreditLimitofLeave(String leaveType, String onDate, String vacType) throws Exception {
        long maximumLimit = 0;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = dataSource.getConnection();
            if (vacType != null && !vacType.equals("") && !vacType.equalsIgnoreCase("G")) {
                ps = con.prepareStatement("SELECT DISTINCT MAX_CR FROM g_leave WHERE TOL_ID='" + leaveType + "' AND WEF=(SELECT MAX(WEF) FROM g_leave WHERE TOL_ID='" + leaveType + "' AND WEF<='" + onDate + "') AND VACATION='" + vacType + "'");
                rs = ps.executeQuery();
            } else {
                ps = con.prepareStatement("SELECT DISTINCT MAX_CR FROM g_leave WHERE TOL_ID='" + leaveType + "' AND WEF=(SELECT MAX(WEF) FROM g_leave WHERE TOL_ID='" + leaveType + "' AND WEF<='" + onDate + "') AND VACATION is null");
                rs = ps.executeQuery();
            }
            while (rs.next()) {
                maximumLimit = rs.getInt("MAX_CR");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return maximumLimit;
    }

    public String getSPCOnParticularDate(String empId, String inputDate) throws Exception {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String spc = "";
        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("SELECT spc,join_date FROM EMP_JOIN WHERE SPC IS NOT NULL AND NOT_TYPE!='ADDITIONAL_CHARGE' AND  (if_ad_charge='N' or if_ad_charge is null) and emp_id=? and join_date<=TO_DATE(?,'DD-MON-YYYY') and "
                    + "join_date=(SELECT max(join_date) FROM emp_join WHERE SPC IS NOT NULL AND JOIN_DATE IS NOT NULL AND NOT_TYPE!='ADDITIONAL_CHARGE' AND (if_ad_charge='N' or if_ad_charge is null)  and emp_id=? and join_date<=TO_DATE(?,'DD-MON-YYYY') )");
            ps.setString(1, empId);
            ps.setString(2, inputDate);
            ps.setString(3, empId);
            ps.setString(4, inputDate);
            rs = ps.executeQuery();
            if (rs.next()) {
                spc = rs.getString("spc");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return spc;
    }

    public void deletePeriodicLeaveData(String empId, String tolid) {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("delete from emp_leave_cr where emp_id=? and tol_id=?");
            ps.setString(1, empId);
            ps.setString(2, tolid);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

}
