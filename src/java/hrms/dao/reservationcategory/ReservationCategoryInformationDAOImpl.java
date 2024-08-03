/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.reservationcategory;

//import com.hrms.master.model.disability.Disability;
import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.model.master.Category;
import hrms.model.reservationcategory.ReservationCategoryInformation;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 *
 * @author HP
 */
public class ReservationCategoryInformationDAOImpl implements ReservationCategoryInformationDAO {
    
    @Resource(name = "dataSource")
    protected DataSource dataSource;
    
    private String uploadPath;
    
    public void setUploadPath(String uploadPath) {
        this.uploadPath = uploadPath;
    }
    
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    
    @Override
    public void updateReservationCategoryInformation(ReservationCategoryInformation rci) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        List reservationlist = new ArrayList();
        try {
            con = this.dataSource.getConnection();
            String sql = "update emp_mast SET if_employed_res=?,category=?,ph_code=?,sp_code=?,if_employed_rehab=? where emp_id=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, rci.getCheckEmployed());
            pst.setString(2, rci.getReservationCategory());
            pst.setString(3, rci.getDisableCategory());
            pst.setString(4, rci.getSpecific_Code());
            pst.setString(5, rci.getRehabilationCategory());
            pst.setString(6, rci.getEmpid());
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
    @Override
    public void saveReservationCategoryInformation(ReservationCategoryInformation rci) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public List getReservationCategoryInformation(String EmpId) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        List reservationlist = new ArrayList();
        ReservationCategoryInformation rci = null;
        try {
            
            con = this.dataSource.getConnection();
            String sql = "select emp_notification.*,if_employed_res,category,emp_mast.ph_code disability,emp_mast.sp_code,emp_mast.if_employed_rehab,ph_desc from\n"
                    + "emp_notification \n"
                    + "left outer join emp_mast on emp_notification.emp_id=emp_mast.emp_id\n"
                    + "left outer join g_ph_code on g_ph_code.ph_code=emp_mast.sp_code\n"
                    + "where emp_notification.emp_id=? and not_type='RES_CAT'";
            
            pst = con.prepareStatement(sql);
            pst.setString(1, EmpId);
            rs = pst.executeQuery();
            while (rs.next()) {
                System.out.println("orderno:" + rs.getString("ordno"));
                rci = new ReservationCategoryInformation();
                rci.setNotid(rs.getString("not_id"));
                rci.setOrderno(rs.getString("ordno"));
                rci.setOrderdate(CommonFunctions.getFormattedOutputDate1(rs.getDate("orddt")));
                rci.setDoe(CommonFunctions.getFormattedOutputDate1(rs.getDate("doe")));
                rci.setReservationCategory(rs.getString("category"));
                rci.setDisableCategory(rs.getString("disability"));
                rci.setSpecific_Code(rs.getString("ph_desc"));
                
                reservationlist.add(rci);
                
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return reservationlist;
    }
    
    @Override
    public ReservationCategoryInformation editReservationCategory(String notid) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        ReservationCategoryInformation rci = null;
        try {
            con = this.dataSource.getConnection();
            String sql = "select emp_notification.*,if_employed_res,category,emp_mast.ph_code disability,emp_mast.sp_code,emp_mast.if_employed_rehab,ph_desc from\n"
                    + "emp_notification \n"
                    + "left outer join emp_mast on emp_notification.emp_id=emp_mast.emp_id\n"
                    + "left outer join g_ph_code on g_ph_code.ph_code=emp_mast.sp_code\n"
                    + "where emp_notification.not_id=? and not_type='RES_CAT'";
            
            pst = con.prepareStatement(sql);
            pst.setInt(1, Integer.parseInt(notid));
            rs = pst.executeQuery();
            while (rs.next()) {
                System.out.println("orderno:" + rs.getString("ordno"));
                rci = new ReservationCategoryInformation();
                rci.setNotid(rs.getString("not_id"));
                rci.setHidnotid(rs.getString("not_id"));
                rci.setOrderno(rs.getString("ordno"));
                rci.setOrderdate(CommonFunctions.getFormattedOutputDate1(rs.getDate("orddt")));
                rci.setDoe(CommonFunctions.getFormattedOutputDate1(rs.getDate("doe")));
                rci.setDeptcode(rs.getString("ent_dept"));
                rci.setOffCode(rs.getString("ent_off"));
                rci.setSanctionauthority(rs.getString("ent_auth"));
                rci.setReservationCategory(rs.getString("category"));
                rci.setDisableCategory(rs.getString("disability"));
                rci.setSpecific_Code(rs.getString("sp_code"));
                rci.setNote(rs.getString("note"));
                rci.setRehabilationCategory(rs.getString("if_employed_rehab"));
                rci.setCheckEmployed(rs.getString("if_employed_res"));
                if (rs.getString("if_visible") != null && rs.getString("if_visible").equals("Y")) {
                    rci.setChkNotSBPrint("N");
                } else {
                    rci.setChkNotSBPrint("Y");
                }
                
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return rci;
    }
    
}
