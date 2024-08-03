package hrms.dao.brassallot;

import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.dao.leaveAccount.ServiceBookLanguageDAO;
import hrms.dao.notification.MaxNotificationIdDAO;
import hrms.model.brassallot.BrassAllot;
import hrms.model.brassallot.BrassAllotList;
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

public class BrassAllotDAOImpl implements BrassAllotDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;
    protected MaxNotificationIdDAO maxnotiidDao;
    
    protected ServiceBookLanguageDAO sbDAO;
    
    public MaxNotificationIdDAO getMaxnotiidDao() {
        return maxnotiidDao;
    }

    public void setMaxnotiidDao(MaxNotificationIdDAO maxnotiidDao) {
        this.maxnotiidDao = maxnotiidDao;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setSbDAO(ServiceBookLanguageDAO sbDAO) {
        this.sbDAO = sbDAO;
    }

    @Override
    public void insertBrassAllotData(BrassAllotList brass, String empId) {

        PreparedStatement pst = null;
        Connection con = null;
        ResultSet rs = null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        String startTime = "";
        Calendar cal = Calendar.getInstance();

        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
        startTime = dateFormat.format(cal.getTime());

        try {
            con = dataSource.getConnection();

            pst = con.prepareStatement("SELECT MAX(WEFDT) maxDate FROM EMP_BRASSALLOT WHERE EMP_ID=?");
            pst.setString(1, empId);
            rs = pst.executeQuery();
            if (rs.next()) {
                if (rs.getString("maxDate") != null && !rs.getString("maxDate").equals("")) {
                    Date maxDt = (Date) sdf.parse(CommonFunctions.getFormattedOutputDate1(rs.getDate("maxDate")));
                    Date wefdate = (Date) sdf.parse(brass.getWefDt());
                    if (wefdate.compareTo(maxDt) > 0) {
                        pst = con.prepareStatement("UPDATE EMP_MAST SET BRASS_NO=? WHERE EMP_ID=?");
                        pst.setString(1, brass.getBrassNo().toUpperCase());
                        pst.setString(2, empId);
                        pst.executeUpdate();
                    } else {
                        
                    }
                } else {
                    pst = con.prepareStatement("UPDATE EMP_MAST SET BRASS_NO=? WHERE EMP_ID=?");
                    pst.setString(1, brass.getBrassNo().toUpperCase());
                    pst.setString(2, empId);
                    pst.executeUpdate();
                }
            }

            String brCode = CommonFunctions.getMaxCode("EMP_BRASSALLOT", "BR_ID", con);
            pst = con.prepareStatement("INSERT INTO EMP_BRASSALLOT(BR_ID,NOT_ID,NOT_TYPE,EMP_ID,BRASS_NO,ALLOTED_DIST_CODE,WEFTIME,WEFDT)VALUES(?,?,?,?,?,?,?,?)");
            pst.setString(1, brCode);
            pst.setInt(2, brass.getNotId());
            pst.setString(3, "BRASS_ALLOT");
            pst.setString(4, empId);
            pst.setString(5, brass.getBrassNo().toUpperCase());
            pst.setString(6, brass.getSltDist());
            pst.setString(7, brass.getWefTime());
            pst.setTimestamp(8, new Timestamp(sdf.parse(brass.getWefDt()).getTime()));
            pst.executeUpdate();
            
            String sbLang = sbDAO.getBrassAllotmentDetails(brass);

            pst = con.prepareStatement("UPDATE EMP_NOTIFICATION SET SB_DESCRIPTION=? WHERE NOT_ID=?");
            pst.setString(1, sbLang);
            pst.setInt(2, brass.getNotId());
            pst.execute();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public int updateBrassAllotData(BrassAllotList brass, String empId) {
        int n = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        //String loanId = maxnotiidDao.getMaxNotId();

        String startTime = "";
        Calendar cal = Calendar.getInstance();

        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
        startTime = dateFormat.format(cal.getTime());
        PreparedStatement pst = null;
        Connection con = null;
        ResultSet rs = null;
        try {
            con = dataSource.getConnection();
            //brass.setNotId(loanId);
            pst = con.prepareStatement("SELECT MAX(WEFDT) maxDate FROM EMP_BRASSALLOT WHERE EMP_ID=?");
            pst.setString(1, empId);
            rs = pst.executeQuery();
            if (rs.next()) {
                if (rs.getString("maxDate") != null && !rs.getString("maxDate").equals("")) {
                    Date maxDt = (Date) sdf.parse(CommonFunctions.getFormattedOutputDate1(rs.getDate("maxDate")));
                    Date wefdate = (Date) sdf.parse(brass.getWefDt());
                    if (wefdate.compareTo(maxDt) > 0) {
                        pst = con.prepareStatement("UPDATE EMP_MAST SET BRASS_NO=? WHERE EMP_ID=?");
                        pst.setString(1, brass.getBrassNo().toUpperCase());
                        pst.setString(2, empId);
                        pst.executeUpdate();
                    } else {
                        
                    }
                } else {
                    pst = con.prepareStatement("UPDATE EMP_MAST SET BRASS_NO=? WHERE EMP_ID=?");
                    pst.setString(1, brass.getBrassNo().toUpperCase());
                    pst.setString(2, empId);
                    pst.executeUpdate();
                }
            }

            pst = con.prepareStatement("UPDATE EMP_BRASSALLOT SET NOT_TYPE=?,ALLOTED_DIST_CODE=?,WEFDT=?,WEFTIME=?,BRASS_NO=? WHERE EMP_ID=? AND NOT_ID=?");
            pst.setString(1, "BRASS_ALLOT");
            pst.setString(2, brass.getSltDist());
            pst.setTimestamp(3, new Timestamp(sdf.parse(brass.getWefDt()).getTime()));
            pst.setString(4, brass.getWefTime());
            pst.setString(5, brass.getBrassNo());
            pst.setString(6, empId);
            pst.setInt(7, brass.getNotId());
            n = pst.executeUpdate();
            
            String sbLang = sbDAO.getBrassAllotmentDetails(brass);

            pst = con.prepareStatement("UPDATE EMP_NOTIFICATION SET SB_DESCRIPTION=? WHERE NOT_ID=?");
            pst.setString(1, sbLang);
            pst.setInt(2, brass.getNotId());
            pst.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return n;
    }

    public int deleteBrassAllotData(String brassId) {
        int n = 0;
        Connection con = null;
        PreparedStatement pst = null;
        Statement st = null;
        ResultSet rs = null;
        String notId = null;
        try {
            con = dataSource.getConnection();
            st = con.createStatement();
            rs = st.executeQuery("SELECT NOT_ID FROM EMP_BRASSALLOT WHERE BR_ID='" + brassId + "'");
            if (rs.next()) {
                notId = rs.getString("NOT_ID");
            }
            if(notId != null && !notId.equals("")){
                pst = con.prepareStatement("DELETE FROM EMP_NOTIFICATION WHERE NOT_ID=?");
                pst.setInt(1, Integer.parseInt(notId));
                pst.executeUpdate();
            }
            
            pst = con.prepareStatement("DELETE FROM EMP_BRASSALLOT WHERE BR_ID=?");
            pst.setString(1, brassId);
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return n;
    }

    public BrassAllotList editBrassAllotData(String empId, int notId) {
        BrassAllotList brass = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
        Connection con = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("select spn,empNot.if_visible,empNot.emp_id, empNot.ordno,empNot.orddt,empNot.doe,empNot.toe,empNot.note,empBrass.brass_no,empBrass.br_id,empNot.dept_code,empNot.off_code,empNot.AUTH,empNot.ent_dept,\n"
                    + " empNot.ent_off,empNot.ent_auth,empBrass.alloted_dist_code,empBrass.wefdt,"
                    + " empBrass.weftime, empBrass.not_id from emp_brassallot empBrass "
                    
                    + "   inner join emp_notification empNot"
                    + "   on empBrass.not_id=empNot.not_id "
                    + "   left outer join g_spc on empNot.AUTH=g_spc.spc"
                    + "   where empBrass.emp_id=? and empBrass.not_id=? and empBrass.NOT_TYPE='BRASS_ALLOT' ");

            pst.setString(1, empId);
            pst.setInt(2, notId);

            rs = pst.executeQuery();
            if (rs.next()) {
                brass = new BrassAllotList();
                brass.setNotId(rs.getInt("not_id"));
                brass.setNotType("BRASS_ALLOT");
                brass.setBrId(rs.getString("BR_ID"));
                brass.setEmpId(rs.getString("EMP_ID"));
                brass.setDoe(CommonFunctions.getFormattedOutputDate1(rs.getDate("DOE")));
                brass.setToe(rs.getString("TOE"));
                brass.setOrdno(rs.getString("ORDNO"));
                brass.setOrdDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("ORDDT")));
                /*brass.setSltDept(rs.getString("DEPT_CODE"));
                brass.setSltOffice(rs.getString("OFF_CODE"));*/
                brass.setBrassNo(rs.getString("BRASS_NO"));
                brass.setWefDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("WEFDT")));
                brass.setWefTime(rs.getString("WEFTIME"));
                brass.setSltDist(rs.getString("ALLOTED_DIST_CODE"));
                brass.setNote(rs.getString("NOTE"));
                brass.setHidVerifyingDeptCode(rs.getString("DEPT_CODE"));
                brass.setHidVerifyingOffCode(rs.getString("OFF_CODE"));
                brass.setHidVerifyingSpc(rs.getString("AUTH"));
                if(rs.getString("if_visible") != null && !rs.getString("if_visible").equals("")){
                    if(rs.getString("if_visible").equals("Y")){
                        brass.setChkNotSBPrint("N");
                    }else if(rs.getString("if_visible").equals("N")){
                        brass.setChkNotSBPrint("Y");
                    }
                }
                brass.setAuthVerify(rs.getString("spn"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return brass;
    }

    @Override
    public ArrayList findAllBrassAllot(String empId, BrassAllotList bsallot) {
        List list = null;
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        BrassAllotList brassList = null;
        ArrayList arrlist = new ArrayList();
        String emp_id = bsallot.getEmpId();

        try {
            con = dataSource.getConnection();

            pst = con.prepareStatement("select  emp_notification.emp_id,emp_notification.not_id,emp_notification.ordno, "
                    + "   emp_notification.orddt,emp_notification.doe, "
                    + "   emp_brassallot.wefdt,emp_brassallot.brass_no from emp_brassallot  "
                    + "   inner join emp_notification "
                    + "   on emp_brassallot.not_id=emp_notification.not_id "
                    + "   where emp_brassallot.emp_id=? and emp_brassallot.NOT_TYPE='BRASS_ALLOT' ");
            /*pst = con.prepareStatement("SELECT * FROM EMP_NOTIFICATION where emp_id=? and not_type='LOAN_SANC'");*/
            pst.setString(1, empId);
            rs = pst.executeQuery();

            while (rs.next()) {

                brassList = new BrassAllotList();
                brassList.setNotId(rs.getInt("NOT_ID"));
                brassList.setEmpId(empId);
                brassList.setDoe(CommonFunctions.getFormattedOutputDate1(rs.getDate("DOE")));
                brassList.setOrdno(rs.getString("ORDNO"));
                brassList.setOrdDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("ORDDT")));
                brassList.setWefDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("WEFDT")));
                brassList.setBrassNo(rs.getString("BRASS_NO"));
                arrlist.add(brassList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return arrlist;

    }

}
