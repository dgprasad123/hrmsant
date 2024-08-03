package hrms.dao.absenteestmt;

import hrms.SelectOption;
import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.dao.leaveapply.LeaveApplyDAOImpl;
import hrms.model.absentee.Absentee;
import hrms.model.absentee.ExcelImportBean;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;


public class EmpAbsenteeDAOImpl implements EmpAbsenteeDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    protected LeaveApplyDAOImpl leaveapplyDAO;

    public LeaveApplyDAOImpl getLeaveapplyDAO() {
        return leaveapplyDAO;
    }

    public void setLeaveapplyDAO(LeaveApplyDAOImpl leaveapplyDAO) {
        this.leaveapplyDAO = leaveapplyDAO;
    }

    public static String getServerDoe() {
        String currDate;
        Format formatter;
        formatter = new SimpleDateFormat("dd-MMM-yyyy");
        currDate = formatter.format(new Date());
        return currDate;
    }

    @Override
    public ArrayList getAbseneteeList(String empid, int year, int month) {
        ArrayList emparr = new ArrayList();
        ResultSet rs = null;
        Absentee absForm = null;
        PreparedStatement pst = null;
        String sql = "";
        Connection con = null;
        try {
            con = dataSource.getConnection();
            sql = "SELECT ABS_ID,ABS_FROM,ABS_TO,ENT_EMP,ENT_DATE,TOT_DAY FROM EMP_ABSENTEE where EMP_ID=? and YEAR=? and MONTH=? order by ABS_FROM";
            pst = con.prepareStatement(sql);
            pst.setString(1, empid);
            pst.setString(2, year + "");
            pst.setInt(3, month);
            rs = pst.executeQuery();

            while (rs.next()) {
                absForm = new Absentee();
                absForm.setAbsid(rs.getString("ABS_ID"));
                absForm.setFromDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("ABS_FROM")));
                absForm.setToDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("ABS_TO")));
                absForm.setTotaldays(rs.getInt("TOT_DAY"));
                emparr.add(absForm);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return emparr;
    }

    @Override
    public Absentee getAbsenteeDetail(int absid) {
        Absentee absentee = new Absentee();
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("SELECT ABS_ID,ABS_FROM,ABS_TO,ENT_EMP,ENT_DATE,TOT_DAY,YEAR,MONTH FROM EMP_ABSENTEE where ABS_ID=?");
            pst.setInt(1, absid);
            rs = pst.executeQuery();
            while (rs.next()) {
                absentee.setAbsid(rs.getString("ABS_ID"));
                absentee.setSltyear(rs.getInt("YEAR"));
                absentee.setSltmonth(rs.getInt("MONTH"));
                absentee.setFromDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("ABS_FROM")));
                absentee.setToDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("ABS_TO")));
                absentee.setTotaldays(rs.getInt("TOT_DAY"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return absentee;
    }

    @Override
    public boolean saveAbsenteeData(Absentee abform) {
        PreparedStatement pst = null;
        Connection con = null;
        int i;
        abform.setEntDate(new Date());
        try {

            if (getChkDuplicate(abform.getFromDate(), abform.getToDate(), abform.getAbsid(), abform.getEmpId()) == false) {
                con = dataSource.getConnection();

                long difference = new SimpleDateFormat("dd-MMM-yyyy").parse(abform.getToDate()).getTime() - new SimpleDateFormat("dd-MMM-yyyy").parse(abform.getFromDate()).getTime();
                int totDays = Integer.parseInt((difference / (1000 * 60 * 60 * 24)) + "") + 1;
                
                pst = con.prepareStatement("insert into emp_absentee(ABS_ID,EMP_ID,YEAR,MONTH,ABS_FROM,ABS_TO,ENT_EMP,ENT_DATE,TOT_DAY) values(?,?,?,?,?,?,?,?,?)");
                pst.setBigDecimal(1, getMaxAbsenteeId());
                pst.setString(2, abform.getEmpId());
                pst.setInt(3, abform.getSltyear());
                pst.setInt(4, abform.getSltmonth());
                pst.setDate(5, new java.sql.Date(new SimpleDateFormat("dd-MMM-yyyy").parse(abform.getFromDate()).getTime()));
                pst.setDate(6, new java.sql.Date(new SimpleDateFormat("dd-MMM-yyyy").parse(abform.getToDate()).getTime()));
                pst.setString(7, abform.getEntempId());
                pst.setTimestamp(8, new Timestamp(abform.getEntDate().getTime()));
                pst.setInt(9, totDays);
                pst.executeUpdate();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return false;

    }
    
    @Override
    public boolean updateAbsenteeData(Absentee abform) {
        PreparedStatement pst = null;
        String mCode = "";
        Connection con = null;
        int i;
        try {
            con = dataSource.getConnection();

            if (abform.getAbsid() != null && !abform.getAbsid().equals("")) {

                long difference = new SimpleDateFormat("dd-MMM-yyyy").parse(abform.getToDate()).getTime() - new SimpleDateFormat("dd-MMM-yyyy").parse(abform.getFromDate()).getTime();
                int totDays = Integer.parseInt((difference / (1000 * 60 * 60 * 24)) + "") + 1;

                pst = con.prepareStatement("update EMP_ABSENTEE set YEAR=?, MONTH=?,ABS_FROM=?,ABS_TO=?,ENT_EMP=?,ENT_DATE=?,TOT_DAY=? where ABS_ID=?");
                if (getChkDuplicate(abform.getFromDate(), abform.getToDate(), abform.getAbsid(), abform.getEmpId()) == false) {
                    pst.setInt(1, abform.getSltyear());
                    pst.setInt(2, abform.getSltmonth());
                    pst.setDate(3, new java.sql.Date(new SimpleDateFormat("dd-MMM-yyyy").parse(abform.getFromDate()).getTime()));
                    pst.setDate(4, new java.sql.Date(new SimpleDateFormat("dd-MMM-yyyy").parse(abform.getToDate()).getTime()));
                    pst.setString(5, abform.getEntempId());
                    pst.setDate(6, new java.sql.Date(new SimpleDateFormat("dd-MMM-yyyy").parse(getServerDoe()).getTime()));
                    pst.setInt(7, totDays);
                    pst.setInt(8, Integer.parseInt(abform.getAbsid()));
                    pst.executeUpdate();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return false;
    }

    public boolean deleteAbsEmploye(String empId, String absid) {

        PreparedStatement pst = null;
        //PreparedStatement pstUpdateNotification = null;
        PreparedStatement pstgetDeleteDateRange = null;
        PreparedStatement pstChkDateRangeSubset = null;
        PreparedStatement pstChkDateRangeSuperset = null;
        PreparedStatement pstLeave = null;

        Statement st = null;
        ResultSet queryResult = null;
        ResultSet rs = null;
        Connection con = null;
        String notId = null;
        String leaveId = null;
        Date fdate = null;
        Date tdate = null;
        int ret = 0;
        try {
            con = dataSource.getConnection();
            st = con.createStatement();
            //  pstUpdateNotification = con.prepareStatement("UPDATE EMP_NOTIFICATION SET STATUS = ? WHERE NOT_ID = ?");
            pstLeave = con.prepareStatement("SELECT FDATE,TDATE FROM EMP_LEAVE WHERE LEAVEID=?");
            pstgetDeleteDateRange = con.prepareStatement("SELECT ABS_FROM,ABS_TO FROM EMP_ABSENTEE WHERE ABS_ID=?");
            pstgetDeleteDateRange.setString(1, absid);
            queryResult = pstgetDeleteDateRange.executeQuery();
            while (queryResult.next()) {
                fdate = CommonFunctions.getFormattedOutputDate(queryResult.getDate("ABS_FROM"));
                tdate = CommonFunctions.getFormattedOutputDate(queryResult.getDate("ABS_TO"));
            }

            // Check ABSENTEE_PERIOD IS A SUBSET OF LEAVE_PERIOD  or  ABSENTEE _PERIOD = = LEAVE _PERIOD
            pstChkDateRangeSubset = con.prepareStatement("SELECT LEAVEID,NOT_ID FROM EMP_LEAVE WHERE EMP_ID = ? AND LSOT_ID = '01' AND (FDATE <= TO_DATE(?) AND TDATE >= TO_DATE(?))");
            pstChkDateRangeSubset.setString(1, empId);
            pstChkDateRangeSubset.setDate(2, new java.sql.Date(fdate.getTime()));
            pstChkDateRangeSubset.setDate(3, new java.sql.Date(tdate.getTime()));
            queryResult = pstChkDateRangeSubset.executeQuery();
            while (queryResult.next()) {
                notId = queryResult.getString("NOT_ID");
                leaveId = queryResult.getString("LEAVEID");

//                pstUpdateNotification.setString(1, "SANCTIONED");
//                pstUpdateNotification.setString(2, notId);
//                pstUpdateNotification.execute();
                if (leaveId != null && !leaveId.equals("")) {
                    pstLeave.setInt(1, Integer.parseInt(leaveId));
                    rs = pstLeave.executeQuery();
                    if (rs.next()) {
                        PreparedStatement pstLeaveSDaysUpdate = null;
                        pstLeaveSDaysUpdate = con.prepareStatement("UPDATE EMP_LEAVE SET S_DAYS=? WHERE LEAVEID=?");
                        pstLeaveSDaysUpdate.setInt(1, calculateDateDiff(CommonFunctions.getFormattedOutputDate2(rs.getDate("FDATE")), CommonFunctions.getFormattedOutputDate2(rs.getDate("TDATE"))));
                        pstLeaveSDaysUpdate.setInt(2, Integer.parseInt(leaveId));
                        pstLeaveSDaysUpdate.executeUpdate();
                        DataBaseFunctions.closeSqlObjects(pstLeaveSDaysUpdate);
                    }
                }
            }

            // Check ABSENTEE _PERIOD IS SUPERSET OF LEAVE_PERIOD 
            pstChkDateRangeSuperset = con.prepareStatement("SELECT NOT_ID FROM EMP_LEAVE WHERE EMP_ID = ? AND LSOT_ID = '01' AND ((FDATE > TO_DATE(?) AND TDATE <= TO_DATE(?)) OR (FDATE >= TO_DATE(?) AND TDATE < TO_DATE(?)))");
            pstChkDateRangeSuperset.setString(1, empId);
            pstChkDateRangeSuperset.setDate(2, new java.sql.Date(fdate.getTime()));
            pstChkDateRangeSuperset.setDate(3, new java.sql.Date(tdate.getTime()));
            pstChkDateRangeSuperset.setDate(4, new java.sql.Date(fdate.getTime()));
            pstChkDateRangeSuperset.setDate(5, new java.sql.Date(tdate.getTime()));
            queryResult = pstChkDateRangeSuperset.executeQuery();
            while (queryResult.next()) {
                notId = queryResult.getString("NOT_ID");

//                pstUpdateNotification.setString(1, "SANCTIONED");
//                pstUpdateNotification.setString(2, notId);
//                pstUpdateNotification.execute();
            }

            pst = con.prepareStatement("delete from emp_absentee where abs_id=?");
            pst.setString(1, absid);
            ret = pst.executeUpdate();

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
            DataBaseFunctions.closeSqlObjects(pst, st);
            DataBaseFunctions.closeSqlObjects(rs, queryResult);
            DataBaseFunctions.closeSqlObjects(pstgetDeleteDateRange, pstChkDateRangeSuperset, pstChkDateRangeSubset);
            DataBaseFunctions.closeSqlObjects(pstLeave);
        }
        return true;
    }

    public boolean getChkDuplicate(String frmdate, String todate, String absid, String empid) throws Exception {
        boolean ret = false;
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            con = dataSource.getConnection();
            if (frmdate != null && todate != null) {
                
                Date fromDt = new SimpleDateFormat("dd-MMM-yyyy").parse(frmdate);
                Date toDt = new SimpleDateFormat("dd-MMM-yyyy").parse(todate);
                if (absid != null && !absid.equals("")) {
                    pst = con.prepareStatement("select ABS_FROM from emp_absentee where ABS_FROM::date=? and ABS_TO::date=? and emp_id=? and abs_id != ?");
                    pst.setDate(1, new java.sql.Date(fromDt.getTime()));
                    pst.setDate(2, new java.sql.Date(toDt.getTime()));
                    pst.setString(3,empid);
                    pst.setInt(4,Integer.parseInt(absid));
                    rs = pst.executeQuery();
                    if (rs.next()) {
                        ret = true;
                    }
                }else{
                    pst = con.prepareStatement("select ABS_FROM from emp_absentee where ABS_FROM::date=? and ABS_TO::date=? and emp_id=?");
                    pst.setDate(1, new java.sql.Date(fromDt.getTime()));
                    pst.setDate(2, new java.sql.Date(toDt.getTime()));
                    pst.setString(3,empid);
                    rs = pst.executeQuery();
                    if (rs.next()) {
                        ret = true;
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return ret;
    }

    @Override
    public ArrayList getAbseneteeYear(String empid) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        ArrayList absenteeYear = new ArrayList();
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("SELECT joindate_of_goo, DOE_GOV FROM EMP_MAST WHERE emp_id=?");
            pst.setString(1, empid);
            rs = pst.executeQuery();
            Date curDate = new Date();
            Date joindate_of_goo = new Date();
            if (rs.next()) {
                joindate_of_goo = rs.getDate("DOE_GOV");
            }
            Calendar curcalendar = new GregorianCalendar();
            curcalendar.setTime(curDate);
            int curyear = curcalendar.get(Calendar.YEAR);
            Calendar dojCalendar = new GregorianCalendar();
            dojCalendar.setTime(joindate_of_goo);
            int dojyear = dojCalendar.get(Calendar.YEAR);
            for (int i = dojyear; i <= curyear; i++) {
                SelectOption so = new SelectOption();
                so.setLabel(i + "");
                so.setValue(i + "");
                absenteeYear.add(so);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return absenteeYear;
    }

    public BigDecimal getMaxAbsenteeId() {
        Statement st = null;
        ResultSet rs = null;
        Connection con = null;
        BigDecimal mCode = null;
        try {
            con = this.dataSource.getConnection();
            st = con.createStatement();
            rs = st.executeQuery("SELECT MAX(cast ( ABS_ID as BIGINT ))+1 MCODE FROM EMP_ABSENTEE");
            if (rs.next()) {
                mCode = rs.getBigDecimal("MCODE");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return mCode;
    }

    private int calculateDateDiff(String fromdate, String toDate) {
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        int dateDiff = 0;
        try {
            con = dataSource.getConnection();
            st = con.createStatement();
            
            rs = st.executeQuery("SELECT DATE_PART('day', '" + toDate + "'::timestamp - '" + fromdate + "'::timestamp)+1 DATECOUNT");
            if (rs.next()) {
                dateDiff = rs.getInt("DATECOUNT");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return dateDiff;
    }

    @Override
    public void addExcelRowIntoDB(Workbook workbook, List li, int month, int year, String offCode) {
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        ResultSet searchExistRs = null;
        try {
            con = this.dataSource.getConnection();

            Sheet sheet = workbook.getSheet(0);
            int noofRows = sheet.getRows();
            Cell cell1 = null;
            Cell cell2 = null;
            Cell cell3 = null;
            String empId = null;
            String empName = null;
            int daysWorked = 0;
            ps = con.prepareStatement("INSERT INTO emp_attendance(at_month, at_year"
                    + ", emp_id, office_code, date_imported, days_worked, emp_name, v_month)"
                    + " VALUES(?, ?, ?, ?, current_timestamp, ?, ?, ?)");
            for (int r = 1; r < noofRows; r++) {
                boolean skiprow = false;
                if (skiprow == false) {
                    cell1 = sheet.getCell(0, r);
                    empId = cell1.getContents();    // Test Count + :
                    cell2 = sheet.getCell(1, r);
                    empName = cell2.getContents();
                    cell3 = sheet.getCell(2, r);
                    daysWorked = Integer.parseInt(cell3.getContents());    // Test Count + :
                    int totalRecords = 0;

                    //check duplicates
                    
                    ps.setInt(1, month);
                    ps.setInt(2, year);
                    ps.setString(3, empId);
                    ps.setString(4, offCode);
                    ps.setInt(5, daysWorked);
                    ps.setString(6, empName);
                    ps.setInt(7, (month + 1));
                    if (empId != null && !empId.equals("")) {
                        if (empId.length() == 8) {
                            ps1 = con.prepareStatement("SELECT COALESCE(COUNT(*),0) AS total_rows from emp_attendance"
                                    + " WHERE at_month = ? AND at_year = ? AND emp_id = ? AND office_code = ?");
                            ps1.setInt(1, month);
                            ps1.setInt(2, year);
                            ps1.setString(3, empId);
                            ps1.setString(4, offCode);
                            searchExistRs = ps1.executeQuery();
                            while (searchExistRs.next()) {
                                totalRecords = searchExistRs.getInt("total_rows");
                            }
                            if (totalRecords == 0) {
                                ps.executeUpdate();
                            }
                        }

                    }

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(searchExistRs, ps);
            DataBaseFunctions.closeSqlObjects(ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }
    

    @Override
    public boolean countMonthAttendance(String offCode, int month, int year) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        boolean isExisting = false;
        try {
            con = dataSource.getConnection();
            ps = con.prepareStatement("SELECT COALESCE(COUNT(*),0) AS total_rows FROM emp_attendance"
                    + " WHERE office_code = ? AND at_month = ? AND at_year = ?");
            ps.setString(1, offCode);
            ps.setInt(2, month);
            ps.setInt(3, year);
            rs = ps.executeQuery();
            while (rs.next()) {
                int totalRows = rs.getInt("total_rows");
                if (totalRows > 0) {
                    isExisting = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return isExisting;
    }

    @Override
    public ArrayList getAttendanceList(String offCode, int year, int month) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList attendanceList = new ArrayList();
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT * FROM emp_attendance WHERE office_code = ? AND "
                    + " at_month = ? AND at_year = ?");
            pstmt.setString(1, offCode);
            pstmt.setInt(2, month);
            pstmt.setInt(3, year);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                ExcelImportBean eBean = new ExcelImportBean();
                eBean.setEmpId(rs.getString("emp_id"));
                eBean.setEmpName(rs.getString("emp_name"));
                eBean.setDaysWorked(rs.getString("days_worked") + "");

                attendanceList.add(eBean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return attendanceList;
    }

    @Override
    public void deleteAttendanceList(String offCode, int year, int month) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            con = dataSource.getConnection();
            ps = con.prepareStatement("DELETE FROM emp_attendance"
                    + " WHERE office_code = ? AND at_month = ? AND at_year = ?");
            ps.setString(1, offCode);
            ps.setInt(2, month);
            ps.setInt(3, year);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }
    
    @Override
    public void deleteAbsenteeList(String empid, int absid) {
        
        Connection con = null;
        
        PreparedStatement pst = null;
        ResultSet rs = null;
        try{
            con = this.dataSource.getConnection();
            
            
            
            String sql = "delete from emp_absentee where emp_id=? and abs_id=?";
            pst = con.prepareStatement(sql);
            pst.setString(1,empid);
            pst.setInt(2,absid);
            pst.executeUpdate();
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            DataBaseFunctions.closeSqlObjects(rs,pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }
    
    @Override
    public int getnoofDaysTobeDelete(String empid,int absid){
        Connection con = null;
        int noofdays=0;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try{
            con = this.dataSource.getConnection();
            
            
            
            String sql = " select sum(to_date(to_char(abs_to, 'YYYY/MM/DD'), 'YYYY/MM/DD')-to_date(to_char(abs_from, 'YYYY/MM/DD'), 'YYYY/MM/DD')) totdays  "
                    + "  from emp_absentee where emp_id=? and abs_id=?";
            pst = con.prepareStatement(sql);
            pst.setString(1,empid);
            pst.setInt(2,absid);
            rs=pst.executeQuery();
            if(rs.next()){
                noofdays=rs.getInt("totdays")+1;
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            DataBaseFunctions.closeSqlObjects(rs,pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return noofdays;
    }
    
    @Override
    public int getnoofdaysInLeave(String empid,int year,int month){
        Connection con = null;
        int noofdays=0;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try{
            con = this.dataSource.getConnection();
            
            
            
            String sql = "select sum(to_date(to_char(abs_to, 'YYYY/MM/DD'), 'YYYY/MM/DD')-to_date(to_char(abs_from, 'YYYY/MM/DD'), 'YYYY/MM/DD')) totdays  "
                    + "  from emp_absentee where emp_id=? and year=? and month=?";
            pst = con.prepareStatement(sql);
            pst.setString(1,empid);
            pst.setString(2,year+"");
            pst.setInt(3,month);
            rs=pst.executeQuery();
            if(rs.next()){
                noofdays=rs.getInt("totdays")+1;
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            DataBaseFunctions.closeSqlObjects(rs,pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return noofdays;
    }
    
    @Override
    public int isBilledEmployee(String empid, int year, int month) {
        
        Connection con = null;
        
        PreparedStatement pst = null;
        ResultSet rs = null;
        int payday=0;
        boolean isBilled = false;
        try{
            con = this.dataSource.getConnection();
            
            String sql = "select pay_day from aq_mast" +
                        " inner join bill_mast on aq_mast.bill_no=bill_mast.bill_no where emp_code=? and bill_mast.aq_year=? and bill_mast.aq_month=? and bill_status_id > 2 AND AQ_MAST.GROSS_AMT>0 ";
            pst = con.prepareStatement(sql);
            pst.setString(1,empid);
            pst.setInt(2,year);
            pst.setInt(3,month);
            rs = pst.executeQuery();
            if(rs.next()){
                payday = rs.getInt("pay_day");
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            DataBaseFunctions.closeSqlObjects(rs,pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
      return payday;  
    }
}
