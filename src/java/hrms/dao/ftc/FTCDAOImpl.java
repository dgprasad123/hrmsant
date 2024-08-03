/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.ftc;

import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.dao.leaveAccount.ServiceBookLanguageDAO;
import hrms.model.FTC.sFTCBean;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 *
 * @author DurgaPrasad
 */
public class FTCDAOImpl implements FTCDAO {
    
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
    public List getFTCEntryList(String empId) {
        Connection con = null;
        
        PreparedStatement pst = null;
        ResultSet rs = null;
        
        List ftclist = new ArrayList();
        sFTCBean slBean = null;
        
        try {
            con = dataSource.getConnection();
            
            String sql = "select emp_notification.not_id, emp_notification.ordno, emp_notification.orddt, emp_notification.doe, emp_ltc.* from emp_notification"
                    + " inner join emp_ltc on emp_notification.not_id=emp_ltc.not_id  where emp_notification.emp_id=? and emp_notification.not_type=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, empId);
            pst.setString(2, "FTC");
            rs = pst.executeQuery();
            while (rs.next()) {
                slBean = new sFTCBean();
                slBean.setTxtNotOrdNo(rs.getString("ordno"));
                slBean.setHidNotId(rs.getInt("not_id"));
                slBean.setTxtNotOrdDt(CommonFunctions.getFormattedOutputDate1(rs.getTimestamp("orddt")));
                slBean.setDateofEntry(CommonFunctions.getFormattedOutputDate1(rs.getTimestamp("doe")));
                slBean.setChkHomeTown(rs.getString("home_town"));
                slBean.setFblYear(rs.getString("fbl_year") + "");
                slBean.setTblYear(rs.getString("tbl_year") + "");
                slBean.setFromDate(CommonFunctions.getFormattedOutputDate1(rs.getTimestamp("fdate")));
                slBean.setToDate(CommonFunctions.getFormattedOutputDate1(rs.getTimestamp("tdate")));
                ftclist.add(slBean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return ftclist;
    }
    
    public void saveFTCEntry(String notId, sFTCBean slBean, String deptCode, String offCode, String spc) {
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
            sbDescription = sbDAO.generateFTCLanguage(slBean);
            System.out.println("sbdescription:"+sbDescription);
            
            pst = con.prepareStatement("INSERT INTO EMP_NOTIFICATION(NOT_TYPE,EMP_ID,DOE,ORDNO,ORDDT,DEPT_CODE,OFF_CODE,AUTH,ENT_DEPT,ENT_OFF,ENT_AUTH,IF_VISIBLE, SB_DESCRIPTION, NOTE,organization_type) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, "FTC");
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
            
            pst.setString(13,sbDescription);
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
            pst1.setString(3, "FTC");
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
    public sFTCBean getSFTCDetail(String empId, int notId) {
        Connection con = null;
        
        PreparedStatement pst = null;
        ResultSet rs = null;
        
        sFTCBean slBean = new sFTCBean();
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
                if (rs.getString("if_visible") != null && rs.getString("if_visible").equals("N")) {
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
    
    @Override
    public void updateFTCEntry(sFTCBean slBean) {
        Connection con = null;
        //String sbDescription = null;
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
            String sbDescription = sbDAO.generateFTCLanguage(slBean);
            
            pst = con.prepareStatement("UPDATE EMP_NOTIFICATION SET ORDNO = ?,ORDDT = ?"
                    + ",DEPT_CODE =?,OFF_CODE =?,AUTH = ?"
                    + ",SB_DESCRIPTION = ?, NOTE =?,organization_type=?, if_visible=? WHERE not_id = ?");
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
    
}
